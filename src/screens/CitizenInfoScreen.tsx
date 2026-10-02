import React from 'react';
import { AppLanguage, PanchayatProfile } from '../types';
import { defaultOfficials, defaultProjects } from '../data/maharashtraData';
import { Building, Users, Phone, MapPin, CheckCircle, Clock, ShieldCheck } from 'lucide-react';

interface CitizenInfoScreenProps {
  language: AppLanguage;
  profile: PanchayatProfile;
}

export const CitizenInfoScreen: React.FC<CitizenInfoScreenProps> = ({
  language,
  profile
}) => {
  return (
    <div className="max-w-4xl mx-auto px-4 py-8 space-y-8">
      {/* Village Header Profile */}
      <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs">
        <div className="flex items-center gap-4 mb-6">
          <div className="w-16 h-16 rounded-2xl bg-orange-500 text-white flex items-center justify-center text-3xl font-bold shadow-sm shadow-orange-200">
            🏛️
          </div>
          <div>
            <h1 className="text-2xl font-black text-stone-900">{profile.nameMr}</h1>
            <p className="text-xs text-stone-500 mt-0.5">
              तालुका: {profile.talukaMr} • जिल्हा: {profile.districtMr} • पिनकोड: {profile.pincode}
            </p>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
          <div className="p-3.5 rounded-xl bg-stone-50 border border-stone-200">
            <div className="text-xl font-black text-stone-900">{profile.population.toLocaleString('en-IN')}</div>
            <div className="text-xs text-stone-500 mt-0.5">{language === 'mr' ? 'एकूण लोकसंख्या' : 'Population'}</div>
          </div>
          <div className="p-3.5 rounded-xl bg-stone-50 border border-stone-200">
            <div className="text-xl font-black text-stone-900">{profile.households}</div>
            <div className="text-xs text-stone-500 mt-0.5">{language === 'mr' ? 'एकूण कुटुंबे' : 'Households'}</div>
          </div>
          <div className="p-3.5 rounded-xl bg-stone-50 border border-stone-200">
            <div className="text-xl font-black text-stone-900">{profile.wardsCount}</div>
            <div className="text-xs text-stone-500 mt-0.5">{language === 'mr' ? 'प्रभाग संख्या' : 'Wards'}</div>
          </div>
          <div className="p-3.5 rounded-xl bg-stone-50 border border-stone-200">
            <div className="text-xl font-black text-emerald-600">आदर्श ग्राम</div>
            <div className="text-xs text-stone-500 mt-0.5">{language === 'mr' ? 'दर्जा व श्रेणी' : 'Award Status'}</div>
          </div>
        </div>

        <div className="mt-6 pt-5 border-t border-stone-100 text-xs text-stone-600 space-y-1.5">
          <div className="flex items-start gap-2">
            <MapPin className="w-4 h-4 text-orange-600 shrink-0 mt-0.5" />
            <span>{profile.officeAddress}</span>
          </div>
          <div className="flex items-center gap-2">
            <Clock className="w-4 h-4 text-orange-600 shrink-0" />
            <span>कार्यालयीन वेळ: {profile.officeHours}</span>
          </div>
          <div className="flex items-center gap-2">
            <Phone className="w-4 h-4 text-orange-600 shrink-0" />
            <span>हेल्पलाईन संपर्क: {profile.helplineNumber}</span>
          </div>
        </div>
      </div>

      {/* Elected Officials and Staff */}
      <div>
        <h2 className="text-base font-bold text-stone-900 mb-4 flex items-center gap-2">
          <Users className="w-5 h-5 text-orange-600" />
          <span>{language === 'mr' ? 'ग्रामपंचायत पदाधिकारी व कर्मचारी' : 'Panchayat Officials & Staff'}</span>
        </h2>

        <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-3.5">
          {defaultOfficials.map((off) => (
            <div key={off.id} className="bg-white p-4 rounded-xl border border-stone-200 shadow-xs flex items-center gap-3">
              <div className="w-11 h-11 rounded-full bg-orange-100 text-orange-700 flex items-center justify-center font-bold text-sm shrink-0">
                👤
              </div>
              <div className="truncate">
                <div className="text-xs font-bold text-stone-900 truncate">{off.name}</div>
                <div className="text-[11px] text-orange-700 font-semibold">{off.roleMr}</div>
                <a href={`tel:${off.phone}`} className="text-[11px] text-stone-400 hover:underline flex items-center gap-1 mt-0.5">
                  <Phone className="w-3 h-3" />
                  <span>{off.phone}</span>
                </a>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Ongoing and Completed Village Projects */}
      <div>
        <h2 className="text-base font-bold text-stone-900 mb-4 flex items-center gap-2">
          <Building className="w-5 h-5 text-emerald-600" />
          <span>{language === 'mr' ? 'गावातील मुख्य विकासकामे' : 'Village Development Projects'}</span>
        </h2>

        <div className="space-y-3.5">
          {defaultProjects.map((p) => (
            <div key={p.id} className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs">
              <div className="flex items-start justify-between gap-3 mb-2">
                <div>
                  <span className="text-[10px] font-bold text-stone-500 uppercase tracking-wider block mb-0.5">
                    {p.scheme}
                  </span>
                  <h3 className="text-sm font-bold text-stone-900">{p.titleMr}</h3>
                </div>
                <span
                  className={`px-2.5 py-0.5 rounded-full text-xs font-bold shrink-0 ${
                    p.status === 'COMPLETED'
                      ? 'bg-emerald-100 text-emerald-800'
                      : 'bg-amber-100 text-amber-800'
                  }`}
                >
                  {p.status === 'COMPLETED' ? 'पूर्ण' : 'प्रगतीपथावर'}
                </span>
              </div>

              <div className="flex items-center justify-between text-xs text-stone-500 mb-2">
                <span>मंजूर निधी: <strong className="text-stone-800">{p.budget}</strong></span>
                <span>प्रगती: <strong className="text-stone-800">{p.completionPercent}%</strong></span>
              </div>

              {/* Progress bar */}
              <div className="w-full bg-stone-100 h-2 rounded-full overflow-hidden">
                <div
                  className="bg-emerald-600 h-full rounded-full transition-all"
                  style={{ width: `${p.completionPercent}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
