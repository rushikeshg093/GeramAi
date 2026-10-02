import React from 'react';
import {
  AppLanguage,
  CitizenUser,
  PanchayatProfile,
  Notice,
  Complaint,
  WaterSchedule,
  ScreenDestination
} from '../types';
import {
  Droplets,
  FileText,
  AlertCircle,
  Bell,
  Sparkles,
  Building,
  Phone,
  ArrowRight,
  CheckCircle,
  Clock,
  User,
  ShieldCheck,
  ChevronRight
} from 'lucide-react';

interface CitizenHomeScreenProps {
  language: AppLanguage;
  citizen: CitizenUser;
  profile: PanchayatProfile;
  notices: Notice[];
  complaints: Complaint[];
  waterSchedules: WaterSchedule[];
  onNavigate: (screen: ScreenDestination) => void;
}

export const CitizenHomeScreen: React.FC<CitizenHomeScreenProps> = ({
  language,
  citizen,
  profile,
  notices,
  complaints,
  waterSchedules,
  onNavigate
}) => {
  const latestNotice = notices[0];
  const myComplaints = complaints.filter(
    (c) => c.citizenPhone === citizen.mobileNumber || c.citizenName === citizen.fullName
  );
  const myWardSchedule = waterSchedules.find((w) => w.wardNumber === citizen.wardNumber) || waterSchedules[0];

  return (
    <div className="min-h-screen bg-stone-50 pb-16">
      {/* Citizen Welcome Card */}
      <div className="bg-gradient-to-r from-emerald-700 via-teal-700 to-emerald-800 text-white py-6 px-4">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="flex items-center gap-3.5">
            <div className="w-14 h-14 rounded-2xl bg-white/10 backdrop-blur border border-white/20 flex items-center justify-center text-2xl shrink-0">
              👤
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="px-2 py-0.5 rounded-full bg-emerald-400/20 text-emerald-200 text-[11px] font-bold">
                  {language === 'mr' ? 'नोंदणीकृत ग्रामस्थ' : 'Registered Resident'}
                </span>
                <span className="text-white/70 text-xs">{citizen.wardNumber}</span>
              </div>
              <h1 className="text-xl sm:text-2xl font-black mt-1">
                {language === 'mr' ? `सस्नेह नमस्कार, ${citizen.fullName}` : `Welcome, ${citizen.fullName}`}
              </h1>
              <p className="text-xs text-emerald-100">
                {profile.nameMr} • {profile.officeAddress}
              </p>
            </div>
          </div>

          {/* Quick AI Assistant Button */}
          <button
            onClick={() => onNavigate('CITIZEN_AI_ASSISTANT')}
            className="self-start md:self-center px-4 py-2 rounded-xl bg-white text-emerald-900 hover:bg-emerald-50 text-xs font-bold shadow-sm transition flex items-center gap-2 shrink-0"
          >
            <Sparkles className="w-4 h-4 text-emerald-600 animate-pulse" />
            <span>{language === 'mr' ? 'ग्राममित्र AI सहाय्यक' : 'Ask Gram-Mitra AI'}</span>
          </button>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 py-6 space-y-6">
        {/* Latest Notice Ticker */}
        {latestNotice && (
          <div
            onClick={() => onNavigate('CITIZEN_NOTICES')}
            className="bg-amber-50 border border-amber-200 rounded-2xl p-4 flex items-center justify-between gap-3 cursor-pointer hover:bg-amber-100/70 transition"
          >
            <div className="flex items-center gap-3">
              <span className="p-2 rounded-xl bg-amber-500 text-white shrink-0">
                <Bell className="w-4 h-4" />
              </span>
              <div>
                <span className="text-[10px] font-bold text-amber-800 uppercase tracking-wider block">
                  {language === 'mr' ? 'ताजी ग्रामसभा सूचना' : 'Latest Village Notice'}
                </span>
                <p className="text-xs sm:text-sm font-bold text-stone-900 line-clamp-1">
                  {latestNotice.titleMr}
                </p>
              </div>
            </div>
            <ChevronRight className="w-4 h-4 text-amber-600 shrink-0" />
          </div>
        )}

        {/* 6 Key Village Services Grid */}
        <div>
          <h2 className="text-base font-bold text-stone-900 mb-3 flex items-center gap-2">
            <span>{language === 'mr' ? 'ग्रामपंचायत ई-सेवा कक्ष' : 'Village E-Services'}</span>
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3.5">
            {/* Water Services */}
            <div
              onClick={() => onNavigate('CITIZEN_WATER')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-sky-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-sky-50 text-sky-600 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <Droplets className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-sky-700">
                  {language === 'mr' ? 'पाणीपुरवठा व टँकर' : 'Water & Tankers'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'प्रभागनिहाय वेळापत्रक व त्वरित टँकर बुकिंग' : 'Ward supply timings & tanker booking'}
                </p>
              </div>
            </div>

            {/* Online Certificates */}
            <div
              onClick={() => onNavigate('CITIZEN_SERVICES')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-emerald-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <FileText className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-emerald-700">
                  {language === 'mr' ? 'ऑनलाइन दाखले व सेवा' : 'Online Certificates'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'जन्म, मृत्यू, रहिवासी व BPL दाखले अर्ज' : 'Birth, death, residence & BPL certificates'}
                </p>
              </div>
            </div>

            {/* Complaints */}
            <div
              onClick={() => onNavigate('CITIZEN_COMPLAINTS')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-rose-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <AlertCircle className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-rose-700">
                  {language === 'mr' ? 'तक्रार निवारण मंच' : 'Grievance Redressal'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'रस्ते, वीज, पाणी व कचरा तक्रार नोंदणी' : 'Submit complaints & track resolution'}
                </p>
              </div>
            </div>

            {/* Notices */}
            <div
              onClick={() => onNavigate('CITIZEN_NOTICES')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-purple-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <Bell className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-purple-700">
                  {language === 'mr' ? 'नोटीस बोर्ड व ग्रामसभा' : 'Notice Board'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'ग्रामसभा सूचना, ठराव व शासकीय परिपत्रके' : 'Gram Sabha notices & resolutions'}
                </p>
              </div>
            </div>

            {/* AI Assistant */}
            <div
              onClick={() => onNavigate('CITIZEN_AI_ASSISTANT')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-amber-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <Sparkles className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-amber-700">
                  {language === 'mr' ? 'ग्राममित्र AI सहाय्यक' : 'AI Gram-Mitra'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'शासकीय योजना व दाखले नियमांवर AI मार्गदर्शन' : 'AI assistant for village schemes & rules'}
                </p>
              </div>
            </div>

            {/* Village Info */}
            <div
              onClick={() => onNavigate('CITIZEN_INFO')}
              className="bg-white p-4 sm:p-5 rounded-2xl border border-stone-200 hover:border-stone-500 shadow-xs hover:shadow-md transition cursor-pointer flex flex-col justify-between group"
            >
              <div className="w-11 h-11 rounded-xl bg-stone-100 text-stone-700 flex items-center justify-center mb-3 group-hover:scale-105 transition">
                <Building className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-900 group-hover:text-stone-700">
                  {language === 'mr' ? 'ग्रामपंचायत माहिती' : 'Panchayat Info'}
                </h3>
                <p className="text-[11px] text-stone-500 mt-1 line-clamp-2">
                  {language === 'mr' ? 'सरपंच, ग्रामसेवक, सदस्य व विकास कामे' : 'Elected members & development works'}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Ward Water Supply Snapshot */}
        <div className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
          <div className="flex items-center justify-between mb-3">
            <h3 className="text-sm font-bold text-stone-900 flex items-center gap-2">
              <Droplets className="w-4 h-4 text-sky-600" />
              <span>
                {language === 'mr' ? `आपल्या प्रभागाचे पाणी वेळापत्रक (${myWardSchedule.wardNumber})` : `Your Ward Water Schedule`}
              </span>
            </h3>
            <button
              onClick={() => onNavigate('CITIZEN_WATER')}
              className="text-xs font-bold text-sky-600 hover:text-sky-700"
            >
              {language === 'mr' ? 'संपूर्ण वेळापत्रक →' : 'Full Schedule →'}
            </button>
          </div>

          <div className="grid sm:grid-cols-3 gap-3 text-xs bg-sky-50/50 p-3.5 rounded-xl border border-sky-100">
            <div>
              <span className="text-stone-500 block">सकाळची वेळ:</span>
              <span className="font-bold text-stone-800">{myWardSchedule.morningTime}</span>
            </div>
            <div>
              <span className="text-stone-500 block">सायंकाळची वेळ:</span>
              <span className="font-bold text-stone-800">{myWardSchedule.eveningTime}</span>
            </div>
            <div>
              <span className="text-stone-500 block">स्थिती:</span>
              <span className="font-bold text-emerald-700">✓ {myWardSchedule.status} (जलशुद्धीकरण पूर्ण)</span>
            </div>
          </div>
        </div>

        {/* My Complaints Snapshot */}
        {myComplaints.length > 0 && (
          <div className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-sm font-bold text-stone-900 flex items-center gap-2">
                <AlertCircle className="w-4 h-4 text-rose-600" />
                <span>{language === 'mr' ? 'माझ्या नोंदवलेल्या तक्रारी' : 'My Filed Complaints'}</span>
              </h3>
              <button
                onClick={() => onNavigate('CITIZEN_COMPLAINTS')}
                className="text-xs font-bold text-rose-600 hover:text-rose-700"
              >
                {language === 'mr' ? 'नवीन तक्रार नोंदवा →' : 'New Complaint →'}
              </button>
            </div>

            <div className="space-y-2.5">
              {myComplaints.map((comp) => (
                <div key={comp.id} className="p-3 rounded-xl bg-stone-50 border border-stone-200 text-xs">
                  <div className="flex items-center justify-between mb-1">
                    <span className="font-bold text-stone-900">{comp.title}</span>
                    <span
                      className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                        comp.status === 'RESOLVED'
                          ? 'bg-emerald-100 text-emerald-800'
                          : comp.status === 'IN_PROGRESS'
                          ? 'bg-amber-100 text-amber-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {comp.status}
                    </span>
                  </div>
                  {comp.officerRemarks && (
                    <div className="mt-1 text-stone-600 text-[11px] bg-white p-2 rounded border border-stone-200">
                      <span className="font-semibold text-orange-700">अधिकारी शेरा: </span>
                      {comp.officerRemarks}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Emergency Help Banner */}
        <div className="bg-stone-900 text-white rounded-2xl p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div>
            <h4 className="font-bold text-sm">
              {language === 'mr' ? 'ग्रामपंचायत कार्यालय संपर्क व मदत कक्ष' : 'Panchayat Helpdesk'}
            </h4>
            <p className="text-xs text-stone-400 mt-0.5">
              {profile.officeHours} • सरपंच: {profile.sarpanchPhone}
            </p>
          </div>
          <a
            href={`tel:${profile.sarpanchPhone}`}
            className="px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold flex items-center gap-1.5 shrink-0 transition"
          >
            <Phone className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? 'थेट संपर्क करा' : 'Call Office'}</span>
          </a>
        </div>
      </div>
    </div>
  );
};
