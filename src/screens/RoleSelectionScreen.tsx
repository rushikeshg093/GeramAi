import React from 'react';
import { AppLanguage, PanchayatProfile } from '../types';
import {
  Users,
  Shield,
  ArrowRight,
  Lock,
  CheckCircle2,
  Building2,
  MapPin,
  Landmark
} from 'lucide-react';

interface RoleSelectionScreenProps {
  language: AppLanguage;
  currentProfile: PanchayatProfile;
  onOpenGpSelector: () => void;
  onSelectCitizen: () => void;
  onSelectAdmin: () => void;
}

export const RoleSelectionScreen: React.FC<RoleSelectionScreenProps> = ({
  language,
  currentProfile,
  onOpenGpSelector,
  onSelectCitizen,
  onSelectAdmin
}) => {
  const displayName = language === 'mr' ? currentProfile.nameMr : currentProfile.nameEn;
  const displayTaluka = language === 'mr' ? currentProfile.talukaMr : currentProfile.talukaEn;
  const displayDistrict = language === 'mr' ? currentProfile.districtMr : currentProfile.districtEn;

  return (
    <div className="min-h-[calc(100vh-80px)] py-10 px-4 flex flex-col items-center justify-center bg-stone-50">
      <div className="max-w-4xl w-full">
        {/* Active GP Banner with 3-step hierarchy confirmation */}
        <div className="mb-6 mx-auto max-w-2xl bg-white rounded-2xl border border-orange-200/90 shadow-xs p-3.5 sm:p-4 flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-orange-100 text-orange-700 flex items-center justify-center text-lg font-bold shrink-0">
              <Landmark className="w-5 h-5" />
            </div>
            <div>
              <div className="text-[11px] font-bold uppercase tracking-wider text-orange-600 flex items-center gap-1">
                <MapPin className="w-3 h-3" />
                <span>
                  {displayDistrict} → {displayTaluka}
                </span>
              </div>
              <div className="text-sm sm:text-base font-bold text-stone-900 leading-tight">
                {displayName}
              </div>
            </div>
          </div>
          <button
            type="button"
            onClick={onOpenGpSelector}
            className="px-3 py-1.5 rounded-xl bg-stone-100 hover:bg-orange-50 hover:text-orange-700 hover:border-orange-300 border border-stone-200 text-xs font-bold text-stone-700 transition shrink-0 cursor-pointer"
          >
            {language === 'mr' ? 'गाव / GP बदला' : 'Change GP'}
          </button>
        </div>

        {/* Top Header Banner */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-orange-100 text-orange-800 text-xs font-semibold mb-3">
            <Building2 className="w-3.5 h-3.5" />
            <span>
              {language === 'mr'
                ? `ई-पंचायत डिजिटल सेवा कक्ष • ${displayName}`
                : `Digital E-Panchayat Portal • ${displayName}`}
            </span>
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold text-stone-900 tracking-tight">
            {language === 'mr' ? 'प्रवेश पर्याय निवडा' : 'Select Your Portal Role'}
          </h1>
          <p className="mt-2 text-stone-600 text-sm sm:text-base max-w-xl mx-auto">
            {language === 'mr'
              ? 'कृपया खालीलपैकी आपला योग्य पर्याय निवडून पुढे जा'
              : 'Please choose your role to access the appropriate dashboard'}
          </p>
        </div>

        {/* 2 Clear Cards */}
        <div className="grid md:grid-cols-2 gap-6">
          {/* Card 1: Citizen */}
          <div
            onClick={onSelectCitizen}
            className="group relative bg-white rounded-3xl border-2 border-stone-200 hover:border-emerald-500 p-6 sm:p-8 shadow-sm hover:shadow-lg transition cursor-pointer flex flex-col justify-between"
          >
            <div>
              <div className="flex items-center justify-between mb-5">
                <div className="w-14 h-14 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center group-hover:scale-105 transition">
                  <Users className="w-7 h-7" />
                </div>
                <span className="px-3 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-800">
                  {language === 'mr' ? 'नागरिक प्रवेश' : 'Citizen Access'}
                </span>
              </div>

              <h2 className="text-2xl font-bold text-stone-900 group-hover:text-emerald-700 transition">
                {language === 'mr' ? '१. नागरिक (Citizen)' : '1. Citizen Portal'}
              </h2>
              <p className="mt-2 text-stone-600 text-sm leading-relaxed">
                {language === 'mr'
                  ? `${displayName} गावातील नागरिक, शेतकरी व कुटुंबीयांसाठी ई-सेवा, पाणीपुरवठा व तक्रार निवारण कक्ष.`
                  : `Universal public portal for registered village residents, farmers and citizens.`}
              </p>

              <div className="mt-5 space-y-2.5 pt-4 border-t border-stone-100 text-xs text-stone-600">
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'जिल्हा → तालुका → ग्रामपंचायत निवड व नोंदणी'
                      : 'District → Taluka → GP selection & registration'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'जन्म, मृत्यू, रहिवासी व BPL ऑनलाइन दाखले'
                      : 'Birth, death, residence & BPL certificates'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'प्रभागनिहाय पाणीपुरवठा वेळापत्रक व टँकर मागणी'
                      : 'Ward water supply schedules & tanker booking'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'तक्रार निवारण व ग्राममित्र AI सहाय्यक'
                      : 'Grievance redressal & AI Gram-Mitra assistant'}
                  </span>
                </div>
              </div>
            </div>

            <div className="mt-8">
              <button
                type="button"
                className="w-full flex items-center justify-center gap-2 py-3 px-4 rounded-xl bg-emerald-600 group-hover:bg-emerald-700 text-white font-bold text-sm shadow-sm transition cursor-pointer"
              >
                <span>{language === 'mr' ? 'नागरिक म्हणून पुढे जा' : 'Continue as Citizen'}</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          </div>

          {/* Card 2: Admin / Officer */}
          <div
            onClick={onSelectAdmin}
            className="group relative bg-white rounded-3xl border-2 border-stone-200 hover:border-orange-500 p-6 sm:p-8 shadow-sm hover:shadow-lg transition cursor-pointer flex flex-col justify-between"
          >
            <div>
              <div className="flex items-center justify-between mb-5">
                <div className="w-14 h-14 rounded-2xl bg-orange-50 text-orange-600 flex items-center justify-center group-hover:scale-105 transition">
                  <Shield className="w-7 h-7" />
                </div>
                <span className="px-3 py-1 rounded-full text-xs font-bold bg-amber-100 text-amber-800 flex items-center gap-1">
                  <Lock className="w-3 h-3" />
                  {language === 'mr' ? 'अधिकारी मर्यादित' : 'Restricted Officer'}
                </span>
              </div>

              <h2 className="text-2xl font-bold text-stone-900 group-hover:text-orange-700 transition">
                {language === 'mr' ? '२. अधिकारी / प्रशासक (Admin / Officer)' : '2. Officer / Admin Portal'}
              </h2>
              <p className="mt-2 text-stone-600 text-sm leading-relaxed">
                {language === 'mr'
                  ? 'सरपंच, ग्रामसेवक, तलाठी व अधिकृत अधिकाऱ्यांसाठी स्वतंत्र, विलग (Isolated) प्रशासकीय व्यवस्थापन कक्ष.'
                  : 'Strictly isolated administrative console for Sarpanch, Gramsevak, Talathi and staff.'}
              </p>

              <div className="mt-5 space-y-2.5 pt-4 border-t border-stone-100 text-xs text-stone-600">
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-orange-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'अधिकारी नेमणूक एकाच ग्रामपंचायतीशी अधिकृत जोडलेली'
                      : 'Officers strictly linked to exactly one Gram Panchayat'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-orange-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'अधिकारी आयडी / मोबाईल + पासवर्ड + सुरक्षित OTP'
                      : 'Admin ID / Mobile + Password + 2FA OTP'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-orange-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? 'केवळ आपल्या ग्रामपंचायतीचाच डेटा दृश्यमान (Isolated)'
                      : 'Isolated Gram Panchayat data & complaints view'}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-orange-500 shrink-0" />
                  <span>
                    {language === 'mr'
                      ? '🤖 AI स्वयंचलित व्हॉइस कॉल ब्रॉडकास्ट सिस्टीम'
                      : '🤖 AI automated citizen voice call system'}
                  </span>
                </div>
              </div>
            </div>

            <div className="mt-8">
              <button
                type="button"
                className="w-full flex items-center justify-center gap-2 py-3 px-4 rounded-xl bg-orange-600 group-hover:bg-orange-700 text-white font-bold text-sm shadow-sm transition cursor-pointer"
              >
                <span>{language === 'mr' ? 'अधिकारी म्हणून लॉगिन करा' : 'Sign In as Officer'}</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
