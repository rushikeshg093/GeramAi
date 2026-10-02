import React, { useState } from 'react';
import { AppLanguage, GramPanchayat } from '../types';
import { GpHierarchySelector } from './GpHierarchySelector';
import { getDistrictById, getTalukaById, getGramPanchayatById } from '../data/maharashtraData';
import { X, CheckCircle2, Landmark, AlertCircle } from 'lucide-react';

interface GpChangeModalProps {
  language: AppLanguage;
  isOpen: boolean;
  onClose: () => void;
  currentGpId: string;
  onSelectGpContext: (gpId: string) => void;
}

export const GpChangeModal: React.FC<GpChangeModalProps> = ({
  language,
  isOpen,
  onClose,
  currentGpId,
  onSelectGpContext
}) => {
  const currentGp = getGramPanchayatById(currentGpId);
  const currentTaluka = currentGp ? getTalukaById(currentGp.talukaId) : undefined;
  const currentDist = currentTaluka ? getDistrictById(currentTaluka.districtId) : undefined;

  const [tempDistrictId, setTempDistrictId] = useState(currentDist?.id || '472');
  const [tempTalukaId, setTempTalukaId] = useState(currentTaluka?.id || '3983');
  const [tempGpId, setTempGpId] = useState(currentGpId || '172627');
  const [tempGpObj, setTempGpObj] = useState<GramPanchayat | null>(currentGp || null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleApply = () => {
    if (!tempGpId) {
      setErrorMsg(
        language === 'mr'
          ? 'कृपया ग्रामपंचायत निवडा (Select Gram Panchayat)'
          : 'Please select a Gram Panchayat'
      );
      return;
    }
    onSelectGpContext(tempGpId);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-stone-900/60 backdrop-blur-xs animate-in fade-in duration-150">
      <div className="bg-white rounded-3xl max-w-lg w-full p-6 shadow-2xl border border-stone-200">
        <div className="flex items-center justify-between pb-4 border-b border-stone-100">
          <div className="flex items-center gap-2.5">
            <div className="w-10 h-10 rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center font-bold text-lg">
              <Landmark className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold text-stone-900">
                {language === 'mr' ? 'ग्रामपंचायत कार्यक्षेत्र निवडा' : 'Select Gram Panchayat Jurisdiction'}
              </h2>
              <p className="text-xs text-stone-500">
                {language === 'mr'
                  ? 'महाराष्ट्र → जिल्हा → तालुका → ग्रामपंचायत'
                  : 'Maharashtra → District → Taluka → Gram Panchayat'}
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 rounded-xl text-stone-400 hover:text-stone-700 hover:bg-stone-100 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="py-5 space-y-4">
          <p className="text-xs text-stone-600 leading-relaxed">
            {language === 'mr'
              ? 'आपल्या गावाच्या ग्रामपंचायतीची माहिती, जाहीर सूचना, पाणीपुरवठा वेळापत्रक, तक्रारी व दाखल्यांसाठी खालील ३-टप्पे निवडीतून आपली ग्रामपंचायत निवडा:'
              : 'Choose your Gram Panchayat from the 3-step administrative selection below to view local information, notices, water supply schedules, and grievance services:'}
          </p>

          {errorMsg && (
            <div className="p-3 rounded-xl bg-red-50 border border-red-200 text-xs text-red-700 flex items-center gap-2">
              <AlertCircle className="w-4 h-4 text-red-500 shrink-0" />
              <span>{errorMsg}</span>
            </div>
          )}

          <GpHierarchySelector
            language={language}
            selectedDistrictId={tempDistrictId}
            selectedTalukaId={tempTalukaId}
            selectedGpId={tempGpId}
            onSelectDistrict={(dist) => {
              setTempDistrictId(dist);
              setErrorMsg(null);
            }}
            onSelectTaluka={(tal) => {
              setTempTalukaId(tal);
              setErrorMsg(null);
            }}
            onSelectGp={(gpId, gp) => {
              setTempGpId(gpId);
              setTempGpObj(gp);
              setErrorMsg(null);
            }}
            variant="card"
          />
        </div>

        <div className="flex items-center justify-end gap-2.5 pt-4 border-t border-stone-100">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2.5 rounded-xl border border-stone-200 text-stone-700 text-xs font-bold hover:bg-stone-50 transition"
          >
            {language === 'mr' ? 'रद्द करा' : 'Cancel'}
          </button>
          <button
            type="button"
            onClick={handleApply}
            className="px-5 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shadow-sm transition flex items-center gap-1.5"
          >
            <CheckCircle2 className="w-4 h-4" />
            <span>{language === 'mr' ? 'लागू करा (Apply)' : 'Apply'}</span>
          </button>
        </div>
      </div>
    </div>
  );
};
