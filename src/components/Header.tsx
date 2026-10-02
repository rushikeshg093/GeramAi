import React from 'react';
import { AppLanguage, AdminUser, CitizenUser, ScreenDestination, PanchayatProfile } from '../types';
import { Languages, LogOut, Shield, User, ArrowLeft, MapPin } from 'lucide-react';

interface HeaderProps {
  language: AppLanguage;
  onToggleLanguage: () => void;
  adminUser: AdminUser | null;
  citizenUser: CitizenUser | null;
  currentScreen: ScreenDestination;
  onNavigate: (screen: ScreenDestination) => void;
  onLogout: () => void;
  currentProfile: PanchayatProfile;
  onOpenGpSelector?: () => void;
}

export const Header: React.FC<HeaderProps> = ({
  language,
  onToggleLanguage,
  adminUser,
  citizenUser,
  currentScreen,
  onNavigate,
  onLogout,
  currentProfile,
  onOpenGpSelector
}) => {
  const isAuthOrSelection =
    currentScreen === 'ROLE_SELECTION' ||
    currentScreen === 'CITIZEN_AUTH' ||
    currentScreen === 'ADMIN_LOGIN' ||
    currentScreen === 'ADMIN_ACTIVATION';

  const displayName = language === 'mr' ? currentProfile.nameMr : currentProfile.nameEn;
  const displayTaluka = language === 'mr' ? currentProfile.talukaMr : currentProfile.talukaEn;
  const displayDistrict = language === 'mr' ? currentProfile.districtMr : currentProfile.districtEn;

  return (
    <header className="sticky top-0 z-40 bg-white/95 backdrop-blur border-b border-stone-200 shadow-xs">
      {/* Top Govt of Maharashtra strip */}
      <div className="bg-gradient-to-r from-orange-600 via-amber-600 to-orange-700 text-white text-xs py-1 px-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between gap-2">
          <span className="font-medium tracking-wide truncate">
            {language === 'mr'
              ? 'महाराष्ट्र शासन • ग्रामविकास व पंचायत राज विभाग • ई-ग्राम मंच'
              : 'Govt. of Maharashtra • Rural Development & Panchayati Raj'}
          </span>
          <div className="flex items-center gap-2 shrink-0">
            <span className="bg-white/20 px-2 py-0.5 rounded text-[10px] font-semibold flex items-center gap-1">
              <MapPin className="w-3 h-3" />
              <span>
                {displayDistrict} → {displayTaluka} → {displayName}
              </span>
            </span>
            {onOpenGpSelector && !adminUser && (
              <button
                onClick={onOpenGpSelector}
                className="bg-white/25 hover:bg-white/35 px-2 py-0.5 rounded text-[10px] font-bold text-white transition cursor-pointer"
                title={language === 'mr' ? 'ग्रामपंचायत बदला' : 'Change Gram Panchayat'}
              >
                {language === 'mr' ? 'बदला' : 'Change'}
              </button>
            )}
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 py-2.5 flex items-center justify-between gap-2">
        <div className="flex items-center gap-3">
          {/* Back button if in subscreen */}
          {!isAuthOrSelection && currentScreen !== 'CITIZEN_HOME' && currentScreen !== 'ADMIN_DASHBOARD' && (
            <button
              onClick={() => onNavigate(adminUser ? 'ADMIN_DASHBOARD' : 'CITIZEN_HOME')}
              className="p-1.5 rounded-lg hover:bg-stone-100 text-stone-600 transition"
              title={language === 'mr' ? 'मागे जा' : 'Back'}
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
          )}

          <div
            onClick={() => {
              if (adminUser) onNavigate('ADMIN_DASHBOARD');
              else if (citizenUser) onNavigate('CITIZEN_HOME');
              else onNavigate('ROLE_SELECTION');
            }}
            className="flex items-center gap-2.5 cursor-pointer group"
          >
            <div className="w-10 h-10 rounded-xl bg-orange-500 text-white flex items-center justify-center font-bold text-lg shadow-sm shadow-orange-200 group-hover:scale-105 transition shrink-0">
              🏛️
            </div>
            <div>
              <div className="text-sm sm:text-base font-bold text-stone-900 leading-tight">
                {displayName}
              </div>
              <div className="text-xs text-stone-500 flex items-center gap-1.5 flex-wrap">
                <span>
                  {language === 'mr'
                    ? `ता. ${displayTaluka}, जि. ${displayDistrict}`
                    : `Taluka ${displayTaluka}, Dist. ${displayDistrict}`}
                </span>
                <span className="inline-block w-1 h-1 rounded-full bg-stone-300" />
                <span className="text-orange-600 font-medium">
                  {adminUser
                    ? language === 'mr'
                      ? 'प्रशासकीय कक्ष (Isolated)'
                      : 'Admin Portal (Isolated)'
                    : language === 'mr'
                    ? 'नागरिक सेवा केंद्र'
                    : 'Citizen Portal'}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Right side controls */}
        <div className="flex items-center gap-2">
          {/* Language Toggle */}
          <button
            onClick={onToggleLanguage}
            className="flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg border border-stone-200 bg-stone-50 hover:bg-stone-100 text-xs font-semibold text-stone-700 transition"
            title="भाषा बदला / Change Language"
          >
            <Languages className="w-3.5 h-3.5 text-orange-600" />
            <span>{language === 'mr' ? 'English' : 'मराठी'}</span>
          </button>

          {/* User state */}
          {adminUser ? (
            <div className="flex items-center gap-2">
              <div className="hidden md:flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-orange-50 border border-orange-200 text-orange-800 text-xs font-medium">
                <Shield className="w-3.5 h-3.5 text-orange-600" />
                <span className="truncate max-w-[120px]">{adminUser.designation || 'अधिकारी'}</span>
              </div>
              <button
                onClick={onLogout}
                className="flex items-center gap-1 px-3 py-1.5 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-700 text-xs font-semibold transition"
              >
                <LogOut className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">{language === 'mr' ? 'लॉग आउट' : 'Logout'}</span>
              </button>
            </div>
          ) : citizenUser ? (
            <div className="flex items-center gap-2">
              <div className="hidden md:flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-medium">
                <User className="w-3.5 h-3.5 text-emerald-600" />
                <span className="truncate max-w-[120px]">{citizenUser.fullName}</span>
              </div>
              <button
                onClick={onLogout}
                className="flex items-center gap-1 px-3 py-1.5 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-700 text-xs font-semibold transition"
              >
                <LogOut className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">{language === 'mr' ? 'बाहेर पडा' : 'Exit'}</span>
              </button>
            </div>
          ) : !isAuthOrSelection ? (
            <button
              onClick={() => onNavigate('ROLE_SELECTION')}
              className="px-3 py-1.5 rounded-lg bg-orange-600 text-white text-xs font-semibold hover:bg-orange-700 transition"
            >
              {language === 'mr' ? 'प्रवेश करा' : 'Sign In'}
            </button>
          ) : null}
        </div>
      </div>
    </header>
  );
};
