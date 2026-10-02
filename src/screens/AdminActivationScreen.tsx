import React, { useState } from 'react';
import { AppLanguage, PreapprovedOfficer, GramPanchayat } from '../types';
import { preapprovedOfficers, getGramPanchayatById, getDistrictById, getTalukaById } from '../data/maharashtraData';
import { GpHierarchySelector } from '../components/GpHierarchySelector';
import {
  ShieldCheck,
  CheckCircle2,
  Lock,
  Eye,
  EyeOff,
  Search,
  ArrowLeft,
  ArrowRight,
  AlertTriangle,
  Building,
  KeyRound,
  Landmark,
  MapPin
} from 'lucide-react';

interface AdminActivationScreenProps {
  language: AppLanguage;
  onActivate: (
    adminIdOrMobile: string,
    otp: string,
    password: string
  ) => Promise<{ success: boolean; error?: string; officer?: PreapprovedOfficer }>;
  onBackToLogin: () => void;
}

export const AdminActivationScreen: React.FC<AdminActivationScreenProps> = ({
  language,
  onActivate,
  onBackToLogin
}) => {
  const [lookupMode, setLookupMode] = useState<'id' | 'hierarchy'>('id');
  const [queryInput, setQueryInput] = useState('');
  const [selectedDistrictId, setSelectedDistrictId] = useState('472');
  const [selectedTalukaId, setSelectedTalukaId] = useState('3983');
  const [selectedGpId, setSelectedGpId] = useState('172627');

  const [matchedOfficer, setMatchedOfficer] = useState<PreapprovedOfficer | null>(null);
  const [step, setStep] = useState<1 | 2>(1);
  const [otpInput, setOtpInput] = useState('');
  const [sentOtp, setSentOtp] = useState('852963');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [otpSentMsg, setOtpSentMsg] = useState<string | null>(null);

  // Officers filtered by selected Gram Panchayat
  const gpOfficers = preapprovedOfficers.filter(
    (o) =>
      o.gramPanchayatId === selectedGpId ||
      (selectedGpId === '172627' && o.gramPanchayatId === 'gp_palaskhed_daulat') ||
      (selectedGpId === 'gp_palaskhed_daulat' && o.gramPanchayatId === '172627')
  );

  const handleLookup = () => {
    const trimmed = queryInput.trim();
    if (!trimmed) {
      setErrorMessage(
        language === 'mr'
          ? 'कृपया अधिकारी आयडी किंवा नोंदणीकृत मोबाईल नंबर प्रविष्ट करा.'
          : 'Please enter Admin ID or registered mobile number.'
      );
      return;
    }

    const cleanDigits = trimmed.replace(/\D/g, '');
    const found = preapprovedOfficers.find(
      (o) =>
        o.adminId.toLowerCase() === trimmed.toLowerCase() ||
        (cleanDigits.length >= 10 && o.mobileNumber === cleanDigits.slice(-10)) ||
        o.officialEmail.toLowerCase() === trimmed.toLowerCase()
    );

    if (found) {
      selectAndVerifyOfficer(found);
    } else {
      setMatchedOfficer(null);
      setErrorMessage(
        language === 'mr'
          ? 'अनधिकृत प्रवेश: हा मोबाईल नंबर किंवा अधिकारी आयडी सुपर ॲडमिनने तयार केलेल्या कोणत्याही अधिकारी खात्याशी जुळत नाही. प्रत्येक अधिकारी विशिष्ट ग्रामपंचायतीशी जोडलेला असतो.'
          : 'Unauthorized Access: This mobile or Admin ID does not match any officer account. Each officer is strictly linked to one Gram Panchayat.'
      );
    }
  };

  const selectAndVerifyOfficer = (officer: PreapprovedOfficer) => {
    setMatchedOfficer(officer);
    setSelectedDistrictId(officer.districtId);
    setSelectedTalukaId(officer.talukaId);
    setSelectedGpId(officer.gramPanchayatId);
    setErrorMessage(null);
    setStep(2);
    const otp = officer.defaultOtp || '852963';
    setSentOtp(otp);
    setOtpInput(otp);
    setOtpSentMsg(
      language === 'mr'
        ? `अधिकारी पडताळणी यशस्वी (${officer.gramPanchayatNameMr})! OTP: ${otp}`
        : `Officer verified for ${officer.gramPanchayatNameEn}! OTP: ${otp}`
    );
  };

  const handleSubmitActivation = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!matchedOfficer) return;

    if (!otpInput.trim() || otpInput.trim().length < 6) {
      setErrorMessage(
        language === 'mr' ? 'कृपया ६ अंकी अचूक OTP प्रविष्ट करा.' : 'Please enter valid 6-digit OTP.'
      );
      return;
    }

    if (password.length < 6) {
      setErrorMessage(
        language === 'mr'
          ? 'पासवर्ड किमान ६ वर्णांचा असणे आवश्यक आहे.'
          : 'Password must be at least 6 characters long.'
      );
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage(
        language === 'mr' ? 'दोन्ही पासवर्ड जुळत नाहीत.' : 'Passwords do not match.'
      );
      return;
    }

    setIsLoading(true);
    setErrorMessage(null);

    const res = await onActivate(matchedOfficer.adminId, otpInput.trim(), password.trim());
    setIsLoading(false);
    if (!res.success) {
      setErrorMessage(res.error || (language === 'mr' ? 'सक्रियीकरण अयशस्वी' : 'Activation failed'));
    }
  };

  return (
    <div className="min-h-[calc(100vh-80px)] py-8 px-4 flex flex-col items-center justify-center bg-stone-50">
      <div className="max-w-xl w-full">
        {/* Back Link */}
        <button
          onClick={onBackToLogin}
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-stone-500 hover:text-stone-800 mb-4 transition cursor-pointer"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>{language === 'mr' ? 'लॉगिनकडे परत जा' : 'Back to Officer Login'}</span>
        </button>

        <div className="bg-white rounded-3xl border border-stone-200 shadow-sm p-6 sm:p-8">
          {/* Header */}
          <div className="text-center mb-6">
            <div className="w-14 h-14 mx-auto rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center mb-3">
              <ShieldCheck className="w-8 h-8" />
            </div>
            <h1 className="text-2xl font-bold text-stone-900">
              {language === 'mr' ? 'अधिकारी खाते सक्रियीकरण' : 'Officer Account Activation'}
            </h1>
            <p className="mt-1 text-xs text-stone-500">
              {language === 'mr'
                ? 'प्रत्येक अधिकारी केवळ एकाच ग्रामपंचायतीशी अधिकृत जोडलेला असतो'
                : 'Officers are strictly linked to exactly one Gram Panchayat'}
            </p>
          </div>

          {/* Error Banner */}
          {errorMessage && (
            <div className="mb-5 p-3.5 rounded-xl bg-red-50 border border-red-200 flex items-start gap-2.5 text-xs text-red-800">
              <AlertTriangle className="w-4 h-4 text-red-600 shrink-0 mt-0.5" />
              <div className="leading-relaxed">{errorMessage}</div>
            </div>
          )}

          {/* STEP 1: Verify Officer Record */}
          <div className="mb-6 p-4 rounded-2xl bg-stone-50 border border-stone-200">
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs font-bold text-stone-700 flex items-center gap-1.5">
                <span className="w-5 h-5 rounded-full bg-orange-500 text-white flex items-center justify-center text-[10px]">
                  1
                </span>
                {language === 'mr' ? 'पायरी १: अधिकारी पडताळणी' : 'Step 1: Officer Verification'}
              </span>
              {matchedOfficer && (
                <span className="text-xs font-bold text-emerald-600 flex items-center gap-1">
                  <CheckCircle2 className="w-3.5 h-3.5" />
                  {language === 'mr' ? 'पडताळणी पूर्ण' : 'Verified'}
                </span>
              )}
            </div>

            {/* Toggle lookup mode */}
            <div className="flex rounded-xl bg-stone-200/70 p-1 mb-3 text-xs font-semibold">
              <button
                type="button"
                onClick={() => setLookupMode('id')}
                className={`flex-1 py-1.5 rounded-lg transition cursor-pointer ${
                  lookupMode === 'id' ? 'bg-white text-stone-900 shadow-xs' : 'text-stone-600'
                }`}
              >
                {language === 'mr' ? 'आयडी / मोबाईल द्वारे शोधा' : 'Search by ID / Mobile'}
              </button>
              <button
                type="button"
                onClick={() => setLookupMode('hierarchy')}
                className={`flex-1 py-1.5 rounded-lg transition cursor-pointer ${
                  lookupMode === 'hierarchy' ? 'bg-white text-stone-900 shadow-xs' : 'text-stone-600'
                }`}
              >
                {language === 'mr' ? 'जिल्हा → तालुका → GP निवडा' : 'Select District → Taluka → GP'}
              </button>
            </div>

            {lookupMode === 'id' ? (
              <div>
                <p className="text-xs text-stone-500 mb-2">
                  {language === 'mr'
                    ? 'सुपर ॲडमिनने दिलेला अधिकारी आयडी किंवा नोंदणीकृत मोबाईल नंबर टाका:'
                    : 'Enter your Admin ID or registered mobile number:'}
                </p>
                <div className="flex items-center gap-2">
                  <input
                    type="text"
                    value={queryInput}
                    onChange={(e) => {
                      setQueryInput(e.target.value);
                      setErrorMessage(null);
                    }}
                    placeholder="उदा. 9423889900 किंवा OFF-BULD-CHK-001"
                    className="flex-1 px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                  />
                  <button
                    type="button"
                    onClick={handleLookup}
                    className="px-3.5 py-2 bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold rounded-xl transition flex items-center gap-1.5 shrink-0 cursor-pointer"
                  >
                    <Search className="w-3.5 h-3.5" />
                    <span>{language === 'mr' ? 'तपासा' : 'Verify'}</span>
                  </button>
                </div>
              </div>
            ) : (
              <div className="space-y-3">
                <GpHierarchySelector
                  language={language}
                  selectedDistrictId={selectedDistrictId}
                  selectedTalukaId={selectedTalukaId}
                  selectedGpId={selectedGpId}
                  onSelectDistrict={setSelectedDistrictId}
                  onSelectTaluka={setSelectedTalukaId}
                  onSelectGp={(gpId) => setSelectedGpId(gpId)}
                  variant="compact"
                />

                {/* List officers for this GP */}
                <div className="pt-2">
                  <div className="text-[11px] font-bold text-stone-700 mb-1.5">
                    {language === 'mr'
                      ? 'या ग्रामपंचायतीचे पूर्व-मंजूर अधिकारी पदे:'
                      : 'Pre-approved Officer posts for this GP:'}
                  </div>
                  {gpOfficers.length === 0 ? (
                    <p className="text-xs text-stone-400 italic">
                      {language === 'mr'
                        ? 'या ग्रामपंचायतीसाठी अद्याप कोणतेही पद नोंदवलेले नाही.'
                        : 'No pre-approved officer found for this GP.'}
                    </p>
                  ) : (
                    <div className="space-y-1.5">
                      {gpOfficers.map((o) => (
                        <div
                          key={o.adminId}
                          onClick={() => selectAndVerifyOfficer(o)}
                          className="p-2.5 rounded-xl border border-stone-200 bg-white hover:border-orange-400 hover:bg-orange-50/50 transition cursor-pointer flex items-center justify-between"
                        >
                          <div>
                            <div className="font-bold text-xs text-stone-900">{o.fullName}</div>
                            <div className="text-[11px] text-stone-500">{o.designation}</div>
                          </div>
                          <span className="text-[10px] font-bold text-orange-700 bg-orange-100 px-2 py-1 rounded-lg">
                            {language === 'mr' ? 'निवडा' : 'Select'}
                          </span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* Verified Officer Summary Card */}
          {matchedOfficer && (
            <div className="mb-6 p-4 rounded-2xl bg-orange-50 border border-orange-200 animate-in fade-in duration-200">
              <div className="flex items-start gap-3">
                <div className="w-10 h-10 rounded-xl bg-orange-200 text-orange-800 flex items-center justify-center font-bold text-lg shrink-0">
                  🏛️
                </div>
                <div className="text-xs space-y-1">
                  <div className="font-bold text-orange-950 text-sm">{matchedOfficer.fullName}</div>
                  <div className="text-orange-800 font-semibold">{matchedOfficer.designation}</div>
                  <div className="text-stone-700 flex items-center gap-1 font-medium">
                    <Building className="w-3.5 h-3.5 text-stone-500" />
                    <span>
                      {matchedOfficer.gramPanchayatNameMr} (ता. {matchedOfficer.talukaId}, जि.{' '}
                      {matchedOfficer.districtId})
                    </span>
                  </div>
                  <div className="text-stone-500 text-[11px]">
                    मोबाईल: {matchedOfficer.mobileNumber} • आयडी: {matchedOfficer.adminId}
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* STEP 2: OTP & Password Setup */}
          {matchedOfficer && (
            <form onSubmit={handleSubmitActivation} className="space-y-4">
              <div className="p-4 rounded-2xl bg-stone-50 border border-stone-200">
                <div className="flex items-center gap-1.5 text-xs font-bold text-stone-700 mb-3">
                  <span className="w-5 h-5 rounded-full bg-orange-500 text-white flex items-center justify-center text-[10px]">
                    2
                  </span>
                  {language === 'mr'
                    ? 'पायरी २: OTP पडताळणी व सुरक्षित पासवर्ड तयार करा'
                    : 'Step 2: OTP Verification & Set Password'}
                </div>

                {/* OTP */}
                <div className="mb-3">
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr' ? '६ अंकी OTP कोड *' : '6-digit OTP Code *'}
                  </label>
                  <div className="flex items-center gap-2">
                    <input
                      type="text"
                      maxLength={6}
                      value={otpInput}
                      onChange={(e) => setOtpInput(e.target.value.replace(/\D/g, ''))}
                      placeholder="852963"
                      className="flex-1 px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500 font-mono tracking-wider"
                    />
                    <button
                      type="button"
                      onClick={() => {
                        const otp = matchedOfficer.defaultOtp || '852963';
                        setSentOtp(otp);
                        setOtpInput(otp);
                        setOtpSentMsg(
                          language === 'mr' ? `OTP भरला: ${otp}` : `OTP filled: ${otp}`
                        );
                      }}
                      className="px-3 py-2 text-xs font-bold bg-stone-200 hover:bg-stone-300 text-stone-800 rounded-xl transition cursor-pointer"
                    >
                      {language === 'mr' ? 'OTP मिळवा' : 'Get OTP'}
                    </button>
                  </div>
                  {otpSentMsg && (
                    <p className="mt-1 text-[11px] text-emerald-600 font-medium">✓ {otpSentMsg}</p>
                  )}
                </div>

                {/* New Password */}
                <div className="mb-3">
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr'
                      ? 'नवीन पासवर्ड (किमान ६ वर्ण) *'
                      : 'New Password (min 6 chars) *'}
                  </label>
                  <div className="relative">
                    <input
                      type={showPassword ? 'text' : 'password'}
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="उदा. Admin@2026"
                      className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-stone-400 hover:text-stone-600"
                    >
                      {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>
                </div>

                {/* Confirm Password */}
                <div>
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr' ? 'पासवर्ड पुन्हा प्रविष्ट करा *' : 'Confirm Password *'}
                  </label>
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="उदा. Admin@2026"
                    className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                  />
                </div>
              </div>

              {/* Submit Activation */}
              <button
                type="submit"
                disabled={isLoading}
                className="w-full py-3 px-4 rounded-xl bg-orange-600 hover:bg-orange-700 disabled:opacity-50 text-white font-bold text-sm shadow-sm transition flex items-center justify-center gap-2 cursor-pointer"
              >
                {isLoading ? (
                  <span>{language === 'mr' ? 'सक्रियीकरण चालू आहे...' : 'Activating...'}</span>
                ) : (
                  <>
                    <span>
                      {language === 'mr'
                        ? 'अधिकारी खाते सक्रिय करा व लॉगिन करा'
                        : 'Activate Account & Proceed'}
                    </span>
                    <ArrowRight className="w-4 h-4" />
                  </>
                )}
              </button>
            </form>
          )}

          {/* Quick Info note */}
          <div className="mt-6 pt-4 border-t border-stone-100 text-center">
            <p className="text-[11px] text-stone-500">
              {language === 'mr'
                ? 'माहिती: अधिकारी खाते सक्रिय झाल्यानंतर ते आपोआप संबंधित ग्रामपंचायतीच्या प्रशासकीय कक्षाशी जोडले जाते.'
                : 'Note: Once activated, the officer account is isolated to that specific Gram Panchayat.'}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
