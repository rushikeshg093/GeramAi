import React, { useState } from 'react';
import { AppLanguage, PreapprovedOfficer } from '../types';
import { preapprovedOfficers, getGramPanchayatById } from '../data/maharashtraData';
import {
  Shield,
  Lock,
  Eye,
  EyeOff,
  ArrowLeft,
  ArrowRight,
  AlertCircle,
  KeyRound,
  Landmark,
  Building,
  HelpCircle
} from 'lucide-react';

interface AdminLoginScreenProps {
  language: AppLanguage;
  onLogin: (
    adminIdOrMobile: string,
    pass: string,
    otp: string
  ) => Promise<{ success: boolean; error?: string }>;
  onNavigateToActivation: () => void;
  onBackToRoleSelection: () => void;
}

export const AdminLoginScreen: React.FC<AdminLoginScreenProps> = ({
  language,
  onLogin,
  onNavigateToActivation,
  onBackToRoleSelection
}) => {
  const [adminIdOrMobile, setAdminIdOrMobile] = useState('');
  const [password, setPassword] = useState('');
  const [otpInput, setOtpInput] = useState('');
  const [sentOtp, setSentOtp] = useState('852963');
  const [otpStatusMsg, setOtpStatusMsg] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [showForgotModal, setShowForgotModal] = useState(false);
  const [forgotInput, setForgotInput] = useState('');
  const [forgotSuccessMsg, setForgotSuccessMsg] = useState<string | null>(null);

  // Auto-detect matched officer
  const matchedOfficer = preapprovedOfficers.find((o) => {
    const trimmed = adminIdOrMobile.trim().toLowerCase();
    const cleanDigits = trimmed.replace(/\D/g, '');
    return (
      o.adminId.toLowerCase() === trimmed ||
      (cleanDigits.length >= 10 && o.mobileNumber === cleanDigits.slice(-10)) ||
      o.officialEmail.toLowerCase() === trimmed
    );
  });

  const handleSendOtp = () => {
    if (!adminIdOrMobile.trim()) {
      setErrorMessage(
        language === 'mr'
          ? 'कृपया आधी अधिकारी आयडी किंवा मोबाईल नंबर टाका.'
          : 'Please enter Admin ID or mobile number first.'
      );
      return;
    }
    const otpToUse = matchedOfficer?.defaultOtp || '852963';
    setSentOtp(otpToUse);
    setOtpInput(otpToUse);
    setErrorMessage(null);
    setOtpStatusMsg(
      language === 'mr'
        ? `OTP पाठवला: ${otpToUse} (आपोआप भरला)`
        : `OTP sent: ${otpToUse} (auto-filled)`
    );
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!adminIdOrMobile.trim()) {
      setErrorMessage(
        language === 'mr'
          ? 'कृपया अधिकारी आयडी किंवा मोबाईल नंबर प्रविष्ट करा.'
          : 'Please enter Admin ID or Mobile Number.'
      );
      return;
    }
    if (!password.trim()) {
      setErrorMessage(
        language === 'mr' ? 'कृपया पासवर्ड प्रविष्ट करा.' : 'Please enter password.'
      );
      return;
    }

    const effectiveOtp = otpInput.trim() || sentOtp;
    setIsLoading(true);
    setErrorMessage(null);

    const res = await onLogin(adminIdOrMobile.trim(), password.trim(), effectiveOtp);
    setIsLoading(false);
    if (!res.success) {
      setErrorMessage(res.error || (language === 'mr' ? 'लॉगिन अयशस्वी' : 'Login failed'));
    }
  };

  const handleFillPreset = (officer: PreapprovedOfficer) => {
    setAdminIdOrMobile(officer.mobileNumber);
    setPassword(`Admin@${officer.mobileNumber.slice(-4)}`);
    const otpToUse = officer.defaultOtp || '852963';
    setOtpInput(otpToUse);
    setSentOtp(otpToUse);
    setErrorMessage(null);
    setOtpStatusMsg(
      language === 'mr'
        ? `चाचणी लॉगिन तपशील भरले गेले (${officer.gramPanchayatNameMr})`
        : `Test credentials loaded (${officer.gramPanchayatNameEn})`
    );
  };

  return (
    <div className="min-h-[calc(100vh-80px)] py-8 px-4 flex flex-col items-center justify-center bg-stone-50">
      <div className="max-w-md w-full">
        {/* Back Link */}
        <button
          onClick={onBackToRoleSelection}
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-stone-500 hover:text-stone-800 mb-4 transition cursor-pointer"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>{language === 'mr' ? 'भूमिका निवडीकडे परत जा' : 'Back to Role Selection'}</span>
        </button>

        {/* Card */}
        <div className="bg-white rounded-3xl border border-stone-200 shadow-sm p-6 sm:p-8">
          {/* Header */}
          <div className="text-center mb-6">
            <div className="w-14 h-14 mx-auto rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center mb-3">
              <Shield className="w-8 h-8" />
            </div>
            <h1 className="text-2xl font-bold text-stone-900">
              {language === 'mr' ? 'अधिकारी व प्रशासक लॉगिन' : 'Officer / Admin Login'}
            </h1>
            <p className="mt-1 text-xs text-stone-500">
              {language === 'mr'
                ? 'महाराष्ट्र ग्रामपंचायत अधिकृत अधिकारी कक्ष'
                : 'Maharashtra Grampanchayat Official Console'}
            </p>
          </div>

          {/* Matched Officer GP notification badge */}
          {matchedOfficer && (
            <div className="mb-4 p-3 rounded-2xl bg-emerald-50 border border-emerald-200 flex items-start gap-2.5 text-xs text-emerald-900">
              <Landmark className="w-4 h-4 text-emerald-600 shrink-0 mt-0.5" />
              <div>
                <div className="font-bold text-emerald-950">
                  {matchedOfficer.fullName} ({matchedOfficer.designation})
                </div>
                <div className="text-emerald-700 text-[11px] mt-0.5">
                  अधिकृत कार्यक्षेत्र: <strong>{matchedOfficer.gramPanchayatNameMr}</strong> (ता.{' '}
                  {matchedOfficer.talukaId}, जि. {matchedOfficer.districtId})
                </div>
              </div>
            </div>
          )}

          {/* Error Message */}
          {errorMessage && (
            <div className="mb-5 p-3 rounded-xl bg-red-50 border border-red-200 flex items-start gap-2.5 text-xs text-red-700">
              <AlertCircle className="w-4 h-4 text-red-500 shrink-0 mt-0.5" />
              <span>{errorMessage}</span>
            </div>
          )}

          {/* Form */}
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Field 1: Admin ID or Mobile */}
            <div>
              <label className="block text-xs font-bold text-stone-700 mb-1">
                {language === 'mr'
                  ? 'अधिकारी आयडी / नोंदणीकृत मोबाईल नंबर *'
                  : 'Admin ID / Registered Mobile Number *'}
              </label>
              <div className="relative">
                <input
                  type="text"
                  value={adminIdOrMobile}
                  onChange={(e) => {
                    setAdminIdOrMobile(e.target.value);
                    setErrorMessage(null);
                  }}
                  placeholder="उदा. 9423889900 किंवा OFF-BULD-CHK-001"
                  className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                  required
                />
              </div>
            </div>

            {/* Field 2: Password */}
            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="block text-xs font-bold text-stone-700">
                  {language === 'mr' ? 'पासवर्ड *' : 'Password *'}
                </label>
                <button
                  type="button"
                  onClick={() => setShowForgotModal(true)}
                  className="text-xs text-orange-600 hover:text-orange-700 font-semibold cursor-pointer"
                >
                  {language === 'mr' ? 'पासवर्ड विसरलात?' : 'Forgot Password?'}
                </button>
              </div>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-3.5 pr-10 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
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

            {/* Field 3: OTP Code */}
            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="block text-xs font-bold text-stone-700">
                  {language === 'mr' ? '६ अंकी OTP कोड (२-घटक सुरक्षा) *' : '6-digit OTP Code (2FA) *'}
                </label>
                <button
                  type="button"
                  onClick={handleSendOtp}
                  className="text-xs text-orange-600 hover:text-orange-700 font-semibold cursor-pointer"
                >
                  {language === 'mr' ? 'OTP मिळवा' : 'Get OTP'}
                </button>
              </div>
              <div className="flex items-center gap-2">
                <div className="relative flex-1">
                  <KeyRound className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-stone-400" />
                  <input
                    type="text"
                    maxLength={6}
                    value={otpInput}
                    onChange={(e) => setOtpInput(e.target.value.replace(/\D/g, ''))}
                    placeholder="852963"
                    className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500 font-mono tracking-wider"
                  />
                </div>
                <button
                  type="button"
                  onClick={() => {
                    const otpToUse = matchedOfficer?.defaultOtp || '852963';
                    setOtpInput(otpToUse);
                    setSentOtp(otpToUse);
                    setOtpStatusMsg(
                      language === 'mr' ? `OTP भरला: ${otpToUse}` : `OTP filled: ${otpToUse}`
                    );
                  }}
                  className="px-3 py-2 text-xs font-bold bg-stone-100 hover:bg-stone-200 text-stone-700 rounded-xl transition cursor-pointer"
                >
                  {language === 'mr' ? 'ऑटो भरा' : 'Auto Fill'}
                </button>
              </div>
              {otpStatusMsg && (
                <p className="mt-1 text-[11px] text-emerald-600 font-medium">✓ {otpStatusMsg}</p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading}
              className="w-full py-3 px-4 rounded-xl bg-orange-600 hover:bg-orange-700 disabled:opacity-50 text-white font-bold text-sm shadow-sm transition flex items-center justify-center gap-2 cursor-pointer mt-2"
            >
              {isLoading ? (
                <span>{language === 'mr' ? 'पडताळणी चालू आहे...' : 'Verifying...'}</span>
              ) : (
                <>
                  <span>
                    {language === 'mr' ? 'अधिकारी म्हणून प्रवेश करा' : 'Sign In as Officer'}
                  </span>
                  <ArrowRight className="w-4 h-4" />
                </>
              )}
            </button>
          </form>

          {/* Activation Link */}
          <div className="mt-6 pt-5 border-t border-stone-100 text-center">
            <p className="text-xs text-stone-600 mb-2">
              {language === 'mr'
                ? 'प्रथमच प्रवेश करत आहात का? आपले खाते सक्रिय करा:'
                : 'First time signing in? Activate your account:'}
            </p>
            <button
              type="button"
              onClick={onNavigateToActivation}
              className="w-full py-2.5 px-4 rounded-xl border-2 border-orange-200 bg-orange-50 hover:bg-orange-100 text-orange-800 font-bold text-xs transition cursor-pointer"
            >
              {language === 'mr'
                ? '⚡ नवीन अधिकारी खाते सक्रिय करा'
                : '⚡ Activate Officer Account'}
            </button>
          </div>

          {/* Preset testing logins */}
          <div className="mt-5 pt-4 border-t border-stone-100">
            <div className="text-[11px] font-semibold text-stone-500 mb-2 flex items-center gap-1">
              <Building className="w-3.5 h-3.5 text-orange-500" />
              <span>
                {language === 'mr'
                  ? 'चाचणी अधिकारी लॉगिन (क्लिक करून भरा):'
                  : 'Quick Test Officer Accounts (Click to fill):'}
              </span>
            </div>
            <div className="space-y-1.5">
              {preapprovedOfficers.slice(0, 3).map((o) => (
                <button
                  key={o.adminId}
                  type="button"
                  onClick={() => handleFillPreset(o)}
                  className="w-full text-left p-2 rounded-xl bg-stone-50 hover:bg-stone-100 border border-stone-200 text-xs transition flex items-center justify-between cursor-pointer"
                >
                  <div>
                    <span className="font-bold text-stone-900">{o.fullName}</span>
                    <span className="text-[10px] text-stone-500 block">
                      {o.designation} • {o.gramPanchayatNameMr}
                    </span>
                  </div>
                  <span className="text-[10px] text-orange-600 font-semibold bg-orange-50 px-2 py-0.5 rounded-lg border border-orange-200 shrink-0">
                    {o.adminId}
                  </span>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Forgot Password Modal */}
      {showForgotModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-stone-900/60 backdrop-blur-xs">
          <div className="bg-white rounded-2xl max-w-sm w-full p-6 shadow-xl border border-stone-200">
            <h3 className="text-base font-bold text-stone-900 mb-2">
              {language === 'mr' ? 'पासवर्ड रीसेट करा' : 'Reset Password'}
            </h3>
            <p className="text-xs text-stone-600 mb-4">
              {language === 'mr'
                ? 'आपला नोंदणीकृत अधिकारी आयडी किंवा मोबाईल नंबर टाका. रीसेट लिंक/OTP पाठवला जाईल.'
                : 'Enter registered Admin ID or Mobile Number to reset.'}
            </p>
            <input
              type="text"
              value={forgotInput}
              onChange={(e) => setForgotInput(e.target.value)}
              placeholder="उदा. 9423889900"
              className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 mb-3"
            />
            {forgotSuccessMsg && (
              <p className="text-xs text-emerald-600 font-semibold mb-3">✓ {forgotSuccessMsg}</p>
            )}
            <div className="flex items-center justify-end gap-2">
              <button
                type="button"
                onClick={() => {
                  setShowForgotModal(false);
                  setForgotSuccessMsg(null);
                }}
                className="px-3 py-1.5 text-xs text-stone-600 font-semibold hover:bg-stone-100 rounded-lg"
              >
                {language === 'mr' ? 'बंद करा' : 'Close'}
              </button>
              <button
                type="button"
                onClick={() => {
                  setForgotSuccessMsg(
                    language === 'mr'
                      ? 'नवीन तात्पुरता पासवर्ड पाठवला: Admin@2026'
                      : 'Temporary password sent: Admin@2026'
                  );
                }}
                className="px-4 py-1.5 text-xs bg-orange-600 text-white font-bold rounded-lg hover:bg-orange-700"
              >
                {language === 'mr' ? 'रीसेट पाठवा' : 'Send Reset'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
