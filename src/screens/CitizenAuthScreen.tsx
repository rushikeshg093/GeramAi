import React, { useState } from 'react';
import { AppLanguage, CitizenUser, GramPanchayat, Village } from '../types';
import { GpHierarchySelector } from '../components/GpHierarchySelector';
import { getGramPanchayatById, getDistrictById, getTalukaById, getVillagesByGramPanchayat } from '../data/maharashtraData';
import {
  Users,
  Lock,
  Eye,
  EyeOff,
  ArrowLeft,
  ArrowRight,
  AlertCircle,
  Landmark,
  ShieldCheck,
  Check
} from 'lucide-react';

interface CitizenAuthScreenProps {
  language: AppLanguage;
  currentGpId?: string;
  onLogin: (
    mobile: string,
    pass: string,
    selectedGpId?: string
  ) => Promise<{ success: boolean; user?: CitizenUser; error?: string }>;
  onRegister: (
    data: Omit<CitizenUser, 'uid' | 'isRegistered'>,
    pass: string
  ) => Promise<{ success: boolean; user?: CitizenUser; error?: string }>;
  onBackToRoleSelection: () => void;
}

export const CitizenAuthScreen: React.FC<CitizenAuthScreenProps> = ({
  language,
  currentGpId = 'gp_palaskhed_daulat',
  onLogin,
  onRegister,
  onBackToRoleSelection
}) => {
  const initialGp = getGramPanchayatById(currentGpId);
  const initialTaluka = initialGp ? getTalukaById(initialGp.talukaId) : undefined;
  const initialDistrict = initialTaluka ? getDistrictById(initialTaluka.districtId) : undefined;

  const [isRegisterMode, setIsRegisterMode] = useState(false);
  const [mobileNumber, setMobileNumber] = useState('');
  const [password, setPassword] = useState('');
  const [otpInput, setOtpInput] = useState('');
  const [sentOtp, setSentOtp] = useState('852963');
  const [otpSentMsg, setOtpSentMsg] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);

  // Administrative hierarchy selection: Maharashtra -> District -> Taluka -> Gram Panchayat -> Village
  const [selectedDistrictId, setSelectedDistrictId] = useState<string>(initialDistrict?.id || '472');
  const [selectedTalukaId, setSelectedTalukaId] = useState<string>(initialTaluka?.id || '3983');
  const [selectedGpId, setSelectedGpId] = useState<string>(currentGpId || '172627');
  const [selectedGpObj, setSelectedGpObj] = useState<GramPanchayat | null>(initialGp || null);
  const [selectedVillageId, setSelectedVillageId] = useState<string>('529192');
  const [selectedVillageObj, setSelectedVillageObj] = useState<Village | null>(null);

  // Registration specific fields
  const [fullName, setFullName] = useState('');
  const [wardNumber, setWardNumber] = useState('प्रभाग १');
  const [houseNumber, setHouseNumber] = useState('');
  const [isBpl, setIsBpl] = useState(false);
  const [bplNumber, setBplNumber] = useState('');
  const [address, setAddress] = useState('');

  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleSendOtp = () => {
    if (!mobileNumber || mobileNumber.length < 10) {
      setErrorMessage(
        language === 'mr'
          ? 'कृपया वैध १० अंकी मोबाईल नंबर टाका.'
          : 'Please enter valid 10-digit mobile number.'
      );
      return;
    }
    setSentOtp('852963');
    setOtpInput('852963');
    setErrorMessage(null);
    setOtpSentMsg(
      language === 'mr' ? 'OTP पाठवला: 852963 (आपोआप भरला)' : 'OTP sent: 852963 (auto-filled)'
    );
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!selectedGpId) {
      setErrorMessage(
        language === 'mr'
          ? 'कृपया आधी जिल्हा, तालुका व ग्रामपंचायत निवडा.'
          : 'Please select District, Taluka, and Gram Panchayat first.'
      );
      return;
    }

    if (!mobileNumber.trim()) {
      setErrorMessage(language === 'mr' ? 'मोबाईल नंबर आवश्यक आहे.' : 'Mobile number is required.');
      return;
    }
    if (!password.trim()) {
      setErrorMessage(language === 'mr' ? 'पासवर्ड आवश्यक आहे.' : 'Password is required.');
      return;
    }

    setIsLoading(true);
    setErrorMessage(null);

    const gpRecord = selectedGpObj || getGramPanchayatById(selectedGpId);

    if (isRegisterMode) {
      if (!fullName.trim()) {
        setIsLoading(false);
        setErrorMessage(language === 'mr' ? 'पूर्ण नाव आवश्यक आहे.' : 'Full name is required.');
        return;
      }
      if (!otpInput.trim() || otpInput.trim() !== sentOtp) {
        setIsLoading(false);
        setErrorMessage(language === 'mr' ? 'कृपया अचूक OTP प्रविष्ट करा.' : 'Please enter valid OTP.');
        return;
      }

      const res = await onRegister(
        {
          fullName: fullName.trim(),
          mobileNumber: mobileNumber.trim(),
          aadhaarLastFour: '4589',
          isBpl,
          bplNumber: isBpl ? bplNumber.trim() : undefined,
          districtId: selectedDistrictId,
          talukaId: selectedTalukaId,
          gramPanchayatId: selectedGpId,
          gramPanchayatNameMr: gpRecord?.nameMr,
          gramPanchayatNameEn: gpRecord?.nameEn,
          villageId: selectedVillageId,
          villageNameMr: selectedVillageObj?.nameMr,
          villageNameEn: selectedVillageObj?.nameEn,
          wardNumber,
          houseNumber: houseNumber.trim() || 'घर क्र. १२',
          address:
            address.trim() ||
            `${gpRecord?.nameMr || ''}, ता. ${selectedTalukaId}, जि. ${selectedDistrictId}`
        },
        password.trim()
      );
      setIsLoading(false);
      if (!res.success) {
        setErrorMessage(res.error || (language === 'mr' ? 'नोंदणी अयशस्वी' : 'Registration failed'));
      }
    } else {
      const res = await onLogin(mobileNumber.trim(), password.trim(), selectedGpId);
      setIsLoading(false);
      if (!res.success) {
        setErrorMessage(res.error || (language === 'mr' ? 'लॉगिन अयशस्वी' : 'Login failed'));
      }
    }
  };

  const activeGpName =
    selectedGpObj?.nameMr ||
    getGramPanchayatById(selectedGpId)?.nameMr ||
    'आदर्श ग्रामपंचायत पळसखेड दौलत';

  return (
    <div className="min-h-[calc(100vh-80px)] py-8 px-4 flex flex-col items-center justify-center bg-stone-50">
      <div className="max-w-xl w-full">
        {/* Back Link */}
        <button
          onClick={onBackToRoleSelection}
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-stone-500 hover:text-stone-800 mb-4 transition cursor-pointer"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>{language === 'mr' ? 'भूमिका निवडीकडे परत जा' : 'Back to Role Selection'}</span>
        </button>

        <div className="bg-white rounded-3xl border border-stone-200 shadow-sm p-6 sm:p-8">
          {/* Header */}
          <div className="text-center mb-6">
            <div className="w-14 h-14 mx-auto rounded-2xl bg-emerald-100 text-emerald-600 flex items-center justify-center mb-3">
              <Users className="w-8 h-8" />
            </div>
            <h1 className="text-2xl font-bold text-stone-900">
              {isRegisterMode
                ? language === 'mr'
                  ? 'नागरिक ग्रामपंचायत नोंदणी'
                  : 'Citizen GP Registration'
                : language === 'mr'
                ? 'नागरिक प्रवेश (Citizen Login)'
                : 'Citizen Login'}
            </h1>
            <p className="mt-1 text-xs text-stone-500 flex items-center justify-center gap-1">
              <Landmark className="w-3.5 h-3.5 text-emerald-600" />
              <span>{activeGpName}</span>
            </p>
          </div>

          {/* Mode Switch Tabs */}
          <div className="flex rounded-xl bg-stone-100 p-1 mb-6 text-xs font-bold">
            <button
              type="button"
              onClick={() => {
                setIsRegisterMode(false);
                setErrorMessage(null);
              }}
              className={`flex-1 py-2 rounded-lg transition cursor-pointer ${
                !isRegisterMode ? 'bg-white text-stone-900 shadow-xs' : 'text-stone-500 hover:text-stone-800'
              }`}
            >
              {language === 'mr' ? 'नागरिक लॉगिन' : 'Citizen Login'}
            </button>
            <button
              type="button"
              onClick={() => {
                setIsRegisterMode(true);
                setErrorMessage(null);
              }}
              className={`flex-1 py-2 rounded-lg transition cursor-pointer ${
                isRegisterMode ? 'bg-white text-stone-900 shadow-xs' : 'text-stone-500 hover:text-stone-800'
              }`}
            >
              {language === 'mr' ? 'नवीन नोंदणी (Register)' : 'New Registration'}
            </button>
          </div>

          {/* Error Banner */}
          {errorMessage && (
            <div className="mb-4 p-3 rounded-xl bg-red-50 border border-red-200 text-xs text-red-700 flex items-start gap-2">
              <AlertCircle className="w-4 h-4 text-red-500 shrink-0 mt-0.5" />
              <span>{errorMessage}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            {/* MANDATORY 3-STEP ADMINISTRATIVE SELECTION */}
            <div className="p-4 rounded-2xl bg-amber-50/50 border border-amber-200/80">
              <div className="flex items-center justify-between mb-3">
                <span className="text-xs font-bold text-stone-800 flex items-center gap-1.5">
                  <span className="w-5 h-5 rounded-full bg-orange-600 text-white flex items-center justify-center text-[10px]">
                    1
                  </span>
                  {language === 'mr'
                    ? 'ग्रामपंचायत प्रशासकीय निवड (District → Taluka → GP) *'
                    : 'Select Jurisdiction (District → Taluka → GP) *'}
                </span>
                <span className="text-[10px] font-semibold text-emerald-700 bg-emerald-100 px-2 py-0.5 rounded-full">
                  {language === 'mr' ? 'अनिवार्य निवड' : 'Required'}
                </span>
              </div>

              <p className="text-[11px] text-stone-600 mb-3">
                {language === 'mr'
                  ? 'नागरिकाने प्रथम आपला जिल्हा, त्यानंतर तालुका व आपली ग्रामपंचायत निवडणे आवश्यक आहे:'
                  : 'Citizens must first select District, then Taluka, then their Gram Panchayat:'}
              </p>

              <GpHierarchySelector
                language={language}
                selectedDistrictId={selectedDistrictId}
                selectedTalukaId={selectedTalukaId}
                selectedGpId={selectedGpId}
                selectedVillageId={selectedVillageId}
                onSelectDistrict={(dist) => {
                  setSelectedDistrictId(dist);
                  setErrorMessage(null);
                }}
                onSelectTaluka={(tal) => {
                  setSelectedTalukaId(tal);
                  setErrorMessage(null);
                }}
                onSelectGp={(gpId, gp) => {
                  setSelectedGpId(gpId);
                  setSelectedGpObj(gp);
                  setErrorMessage(null);
                }}
                onSelectVillage={(vId, v) => {
                  setSelectedVillageId(vId);
                  setSelectedVillageObj(v);
                  setErrorMessage(null);
                }}
                showVillage={isRegisterMode}
                variant="compact"
              />
            </div>

            {isRegisterMode && (
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'पूर्ण नाव (आधार कार्डप्रमाणे) *' : 'Full Name *'}
                </label>
                <input
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  placeholder="उदा. रमेश सखाराम पाटील"
                  className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-emerald-500"
                  required
                />
              </div>
            )}

            <div>
              <label className="block text-xs font-bold text-stone-700 mb-1">
                {language === 'mr' ? 'नोंदणीकृत मोबाईल नंबर *' : 'Registered Mobile Number *'}
              </label>
              <input
                type="tel"
                maxLength={10}
                value={mobileNumber}
                onChange={(e) => setMobileNumber(e.target.value.replace(/\D/g, ''))}
                placeholder="उदा. 9876543210"
                className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-emerald-500"
                required
              />
            </div>

            {isRegisterMode && (
              <>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="block text-xs font-bold text-stone-700 mb-1">
                      {language === 'mr' ? 'प्रभाग क्रमांक *' : 'Ward No *'}
                    </label>
                    <select
                      value={wardNumber}
                      onChange={(e) => setWardNumber(e.target.value)}
                      className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 bg-white"
                    >
                      <option value="प्रभाग १">प्रभाग १ (जुनी पेठ / गावठाण)</option>
                      <option value="प्रभाग २">प्रभाग २ (शाळा परिसर)</option>
                      <option value="प्रभाग ३">प्रभाग ३ (माळी गल्ली)</option>
                      <option value="प्रभाग ४">प्रभाग ४ (शेतशिवार)</option>
                      <option value="प्रभाग ५">प्रभाग ५ (नवीन वसाहत)</option>
                      <option value="प्रभाग ६">प्रभाग ६ (बाजारपेठ)</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-stone-700 mb-1">
                      {language === 'mr' ? 'घर क्रमांक' : 'House No'}
                    </label>
                    <input
                      type="text"
                      value={houseNumber}
                      onChange={(e) => setHouseNumber(e.target.value)}
                      placeholder="उदा. १२/अ"
                      className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr' ? 'गाव / पत्ता' : 'Village / Address'}
                  </label>
                  <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder={`उदा. ${activeGpName}, ता. ${selectedTalukaId}`}
                    className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300"
                  />
                </div>
              </>
            )}

            <div>
              <label className="block text-xs font-bold text-stone-700 mb-1">
                {language === 'mr' ? 'पासवर्ड *' : 'Password *'}
              </label>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-3.5 pr-10 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-emerald-500"
                  required
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

            {isRegisterMode && (
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'मोबाईल OTP पडताळणी *' : 'Mobile OTP Verification *'}
                </label>
                <div className="flex items-center gap-2">
                  <input
                    type="text"
                    maxLength={6}
                    value={otpInput}
                    onChange={(e) => setOtpInput(e.target.value)}
                    placeholder="852963"
                    className="flex-1 px-3 py-2 text-sm rounded-xl border border-stone-300 font-mono tracking-wider"
                  />
                  <button
                    type="button"
                    onClick={handleSendOtp}
                    className="px-3.5 py-2 rounded-xl bg-stone-100 hover:bg-stone-200 text-stone-700 text-xs font-bold whitespace-nowrap transition cursor-pointer"
                  >
                    {language === 'mr' ? 'OTP मिळवा' : 'Get OTP'}
                  </button>
                </div>
                {otpSentMsg && (
                  <p className="mt-1 text-[11px] text-emerald-600 font-medium">✓ {otpSentMsg}</p>
                )}
              </div>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full py-3 px-4 rounded-xl bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 text-white font-bold text-sm shadow-sm transition flex items-center justify-center gap-2 mt-3 cursor-pointer"
            >
              {isLoading ? (
                <span>{language === 'mr' ? 'प्रक्रिया चालू आहे...' : 'Processing...'}</span>
              ) : (
                <>
                  <span>
                    {isRegisterMode
                      ? language === 'mr'
                        ? 'नागरिक नोंदणी पूर्ण करा'
                        : 'Complete Registration'
                      : language === 'mr'
                      ? 'नागरिक म्हणून प्रवेश करा'
                      : 'Sign In as Citizen'}
                  </span>
                  <ArrowRight className="w-4 h-4" />
                </>
              )}
            </button>
          </form>

          {/* Quick Demo Fill for Citizen */}
          {!isRegisterMode && (
            <div className="mt-6 pt-4 border-t border-stone-100">
              <p className="text-[11px] text-stone-500 mb-2 font-medium text-center">
                {language === 'mr' ? 'जलद चाचणी नागरिक लॉगिन:' : 'Quick Test Citizen Login:'}
              </p>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => {
                    setSelectedDistrictId('buldhana');
                    setSelectedTalukaId('chikhli');
                    setSelectedGpId('gp_palaskhed_daulat');
                    setMobileNumber('9876543210');
                    setPassword('Citizen@123');
                  }}
                  className="px-2.5 py-2 rounded-xl bg-stone-50 hover:bg-stone-100 border border-stone-200 text-stone-700 text-[11px] font-semibold transition text-left cursor-pointer"
                >
                  <div>👤 रमेश पाटील (पळसखेड दौलत)</div>
                  <div className="text-[10px] text-stone-400 font-normal">9876543210 / Citizen@123</div>
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setSelectedDistrictId('pune');
                    setSelectedTalukaId('haveli');
                    setSelectedGpId('gp_wagholi');
                    setMobileNumber('9850112233');
                    setPassword('Wagholi@123');
                  }}
                  className="px-2.5 py-2 rounded-xl bg-stone-50 hover:bg-stone-100 border border-stone-200 text-stone-700 text-[11px] font-semibold transition text-left cursor-pointer"
                >
                  <div>👤 प्रमोद जगताप (वाघोली, पुणे)</div>
                  <div className="text-[10px] text-stone-400 font-normal">9850112233 / Wagholi@123</div>
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
