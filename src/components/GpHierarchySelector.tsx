import React, { useState, useEffect, useMemo } from 'react';
import { AppLanguage, District, Taluka, GramPanchayat, Village } from '../types';
import {
  districts,
  getTalukasByDistrict,
  getGramPanchayatsByTaluka,
  getVillagesByGramPanchayat,
  loadTalukaLgdData,
  normalizeLgdId,
  getDistrictById,
  getTalukaById,
  getGramPanchayatById
} from '../data/maharashtraData';
import { MapPin, Building2, Landmark, Home, Search, Check, Loader2 } from 'lucide-react';

interface GpHierarchySelectorProps {
  language: AppLanguage;
  selectedDistrictId: string;
  selectedTalukaId: string;
  selectedGpId: string;
  selectedVillageId?: string;
  onSelectDistrict: (districtId: string) => void;
  onSelectTaluka: (talukaId: string) => void;
  onSelectGp: (gpId: string, gp: GramPanchayat) => void;
  onSelectVillage?: (villageId: string, village: Village) => void;
  disabled?: boolean;
  className?: string;
  variant?: 'card' | 'inline' | 'compact';
  showVillage?: boolean;
  allowSearch?: boolean;
}

export const GpHierarchySelector: React.FC<GpHierarchySelectorProps> = ({
  language,
  selectedDistrictId,
  selectedTalukaId,
  selectedGpId,
  selectedVillageId = '',
  onSelectDistrict,
  onSelectTaluka,
  onSelectGp,
  onSelectVillage,
  disabled = false,
  className = '',
  variant = 'card',
  showVillage = true,
  allowSearch = true
}) => {
  const [isLoadingGps, setIsLoadingGps] = useState<boolean>(false);
  const [loadedGps, setLoadedGps] = useState<GramPanchayat[]>([]);
  const [loadedVillagesMap, setLoadedVillagesMap] = useState<Record<string, Village[]>>({});

  // Search filter states
  const [districtSearch, setDistrictSearch] = useState('');
  const [talukaSearch, setTalukaSearch] = useState('');
  const [gpSearch, setGpSearch] = useState('');
  const [villageSearch, setVillageSearch] = useState('');

  // Normalize IDs if legacy format was passed
  const normalizedDistId = normalizeLgdId(selectedDistrictId);
  const normalizedTalId = normalizeLgdId(selectedTalukaId);
  const normalizedGpId = normalizeLgdId(selectedGpId);

  // Available talukas based on selected district
  const availableTalukas: Taluka[] = useMemo(() => {
    if (!normalizedDistId) return [];
    return getTalukasByDistrict(normalizedDistId);
  }, [normalizedDistId]);

  // Load GPs and villages when taluka changes
  useEffect(() => {
    if (!normalizedTalId) {
      setLoadedGps([]);
      setLoadedVillagesMap({});
      return;
    }

    // First check synchronous preloaded
    const syncGps = getGramPanchayatsByTaluka(normalizedTalId);
    if (syncGps && syncGps.length > 0) {
      setLoadedGps(syncGps);
      const vMap: Record<string, Village[]> = {};
      syncGps.forEach((gp) => {
        vMap[gp.id] = getVillagesByGramPanchayat(gp.id);
      });
      setLoadedVillagesMap(vMap);
    } else {
      setIsLoadingGps(true);
      loadTalukaLgdData(normalizedTalId)
        .then((data) => {
          setLoadedGps(data.gramPanchayats || []);
          setLoadedVillagesMap(data.villages || {});
          setIsLoadingGps(false);
        })
        .catch(() => {
          setIsLoadingGps(false);
        });
    }
  }, [normalizedTalId]);

  // Available villages mapped to selected GP
  const availableVillages: Village[] = useMemo(() => {
    if (!normalizedGpId) return [];
    if (loadedVillagesMap[normalizedGpId]) {
      return loadedVillagesMap[normalizedGpId];
    }
    return getVillagesByGramPanchayat(normalizedGpId);
  }, [normalizedGpId, loadedVillagesMap]);

  // Filtered lists by search
  const filteredDistricts = useMemo(() => {
    if (!districtSearch.trim()) return districts;
    const q = districtSearch.toLowerCase();
    return districts.filter(
      (d) => d.nameEn.toLowerCase().includes(q) || d.nameMr.includes(q) || (d.code || '').includes(q)
    );
  }, [districtSearch]);

  const filteredTalukas = useMemo(() => {
    if (!talukaSearch.trim()) return availableTalukas;
    const q = talukaSearch.toLowerCase();
    return availableTalukas.filter(
      (t) => t.nameEn.toLowerCase().includes(q) || t.nameMr.includes(q) || (t.code || '').includes(q)
    );
  }, [availableTalukas, talukaSearch]);

  const filteredGps = useMemo(() => {
    if (!gpSearch.trim()) return loadedGps;
    const q = gpSearch.toLowerCase();
    return loadedGps.filter(
      (gp) => gp.nameEn.toLowerCase().includes(q) || gp.nameMr.includes(q) || (gp.code || '').includes(q)
    );
  }, [loadedGps, gpSearch]);

  const filteredVillages = useMemo(() => {
    if (!villageSearch.trim()) return availableVillages;
    const q = villageSearch.toLowerCase();
    return availableVillages.filter(
      (v) => v.nameEn.toLowerCase().includes(q) || v.nameMr.includes(q) || (v.code || '').includes(q)
    );
  }, [availableVillages, villageSearch]);

  const handleDistrictChange = (distId: string) => {
    onSelectDistrict(distId);
    setTalukaSearch('');
    setGpSearch('');
    setVillageSearch('');

    const newTalukas = getTalukasByDistrict(distId);
    if (newTalukas.length > 0) {
      const firstTaluka = newTalukas[0].id;
      onSelectTaluka(firstTaluka);
      loadTalukaLgdData(firstTaluka).then((res) => {
        if (res.gramPanchayats && res.gramPanchayats.length > 0) {
          const firstGp = res.gramPanchayats[0];
          onSelectGp(firstGp.id, firstGp);
          const vList = (res.villages && res.villages[firstGp.id]) || [];
          if (vList.length > 0 && onSelectVillage) {
            onSelectVillage(vList[0].id, vList[0]);
          }
        }
      });
    } else {
      onSelectTaluka('');
      onSelectGp('', {} as GramPanchayat);
      if (onSelectVillage) onSelectVillage('', {} as Village);
    }
  };

  const handleTalukaChange = (talId: string) => {
    onSelectTaluka(talId);
    setGpSearch('');
    setVillageSearch('');

    loadTalukaLgdData(talId).then((res) => {
      if (res.gramPanchayats && res.gramPanchayats.length > 0) {
        const firstGp = res.gramPanchayats[0];
        onSelectGp(firstGp.id, firstGp);
        const vList = (res.villages && res.villages[firstGp.id]) || [];
        if (vList.length > 0 && onSelectVillage) {
          onSelectVillage(vList[0].id, vList[0]);
        }
      } else {
        onSelectGp('', {} as GramPanchayat);
        if (onSelectVillage) onSelectVillage('', {} as Village);
      }
    });
  };

  const handleGpChange = (gpId: string) => {
    const found = loadedGps.find((g) => g.id === gpId);
    if (found) {
      onSelectGp(gpId, found);
      const vList = loadedVillagesMap[gpId] || getVillagesByGramPanchayat(gpId);
      if (vList.length > 0 && onSelectVillage) {
        onSelectVillage(vList[0].id, vList[0]);
      } else if (onSelectVillage) {
        onSelectVillage('', {} as Village);
      }
    }
  };

  const handleVillageChange = (vId: string) => {
    if (!onSelectVillage) return;
    const found = availableVillages.find((v) => v.id === vId);
    if (found) {
      onSelectVillage(vId, found);
    }
  };

  const isCard = variant === 'card';
  const showVillageDropdown = showVillage && Boolean(onSelectVillage);

  return (
    <div
      className={`space-y-4 ${
        isCard
          ? 'p-4 sm:p-5 rounded-2xl bg-orange-50/60 border border-orange-200/80 shadow-xs'
          : ''
      } ${className}`}
    >
      {isCard && (
        <div className="flex items-center justify-between pb-2 border-b border-orange-200/60">
          <div className="flex items-center gap-2 text-stone-900 font-bold text-xs sm:text-sm">
            <span className="flex items-center justify-center w-6 h-6 rounded-lg bg-orange-500 text-white text-xs font-semibold">
              🏛️
            </span>
            <span>
              {language === 'mr'
                ? 'अधिकृत महाराष्ट्र LGD प्रशासकीय रचना'
                : 'Official Maharashtra LGD Hierarchy'}
            </span>
          </div>
          <span className="text-[10px] font-semibold text-orange-700 bg-orange-100 px-2 py-0.5 rounded-full">
            {language === 'mr' ? '४-टप्पे अधिकृत LGD निवड' : '4-Tier Official LGD'}
          </span>
        </div>
      )}

      <div
        className={`grid grid-cols-1 ${
          showVillageDropdown ? 'sm:grid-cols-2 lg:grid-cols-4' : 'sm:grid-cols-3'
        } gap-3`}
      >
        {/* STEP 1: DISTRICT */}
        <div className="space-y-1.5">
          <label className="text-xs font-bold text-stone-700 flex items-center justify-between">
            <span className="flex items-center gap-1.5">
              <span className="w-4 h-4 rounded-full bg-orange-600 text-white flex items-center justify-center text-[10px]">
                १
              </span>
              <MapPin className="w-3.5 h-3.5 text-orange-600" />
              <span>{language === 'mr' ? 'जिल्हा (District) *' : '1. District *'}</span>
            </span>
            {selectedDistrictId && (
              <span className="text-[10px] text-stone-400 font-mono">
                LGD: {normalizedDistId}
              </span>
            )}
          </label>

          {allowSearch && (
            <div className="relative">
              <Search className="w-3 h-3 absolute left-2.5 top-1/2 -translate-y-1/2 text-stone-400" />
              <input
                type="text"
                value={districtSearch}
                onChange={(e) => setDistrictSearch(e.target.value)}
                placeholder={language === 'mr' ? 'जिल्हा शोधा...' : 'Search district...'}
                className="w-full pl-7 pr-2 py-1 text-xs rounded-lg border border-stone-200 bg-white placeholder:text-stone-400 focus:outline-none focus:border-orange-500"
              />
            </div>
          )}

          <select
            value={normalizedDistId}
            onChange={(e) => handleDistrictChange(e.target.value)}
            disabled={disabled}
            className="w-full px-3 py-2 text-xs sm:text-sm rounded-xl border border-stone-300 bg-white font-medium text-stone-900 focus:outline-none focus:border-orange-500 shadow-xs"
          >
            <option value="">
              {language === 'mr' ? '-- जिल्हा निवडा --' : '-- Select District --'}
            </option>
            {filteredDistricts.map((d) => (
              <option key={d.id} value={d.id}>
                {language === 'mr' ? d.nameMr : d.nameEn} ({d.nameEn})
              </option>
            ))}
          </select>
        </div>

        {/* STEP 2: TALUKA */}
        <div className="space-y-1.5">
          <label className="text-xs font-bold text-stone-700 flex items-center justify-between">
            <span className="flex items-center gap-1.5">
              <span className="w-4 h-4 rounded-full bg-amber-600 text-white flex items-center justify-center text-[10px]">
                २
              </span>
              <Building2 className="w-3.5 h-3.5 text-amber-600" />
              <span>{language === 'mr' ? 'तालुका (Taluka) *' : '2. Taluka *'}</span>
            </span>
            {selectedTalukaId && (
              <span className="text-[10px] text-stone-400 font-mono">
                LGD: {normalizedTalId}
              </span>
            )}
          </label>

          {allowSearch && availableTalukas.length > 0 && (
            <div className="relative">
              <Search className="w-3 h-3 absolute left-2.5 top-1/2 -translate-y-1/2 text-stone-400" />
              <input
                type="text"
                value={talukaSearch}
                onChange={(e) => setTalukaSearch(e.target.value)}
                placeholder={language === 'mr' ? 'तालुका शोधा...' : 'Search taluka...'}
                className="w-full pl-7 pr-2 py-1 text-xs rounded-lg border border-stone-200 bg-white placeholder:text-stone-400 focus:outline-none focus:border-amber-500"
              />
            </div>
          )}

          <select
            value={normalizedTalId}
            onChange={(e) => handleTalukaChange(e.target.value)}
            disabled={disabled || !normalizedDistId || availableTalukas.length === 0}
            className="w-full px-3 py-2 text-xs sm:text-sm rounded-xl border border-stone-300 bg-white font-medium text-stone-900 focus:outline-none focus:border-amber-500 shadow-xs disabled:bg-stone-100 disabled:text-stone-400"
          >
            <option value="">
              {!normalizedDistId
                ? language === 'mr'
                  ? '-- आधी जिल्हा निवडा --'
                  : '-- First Select District --'
                : language === 'mr'
                ? '-- तालुका निवडा --'
                : '-- Select Taluka --'}
            </option>
            {filteredTalukas.map((t) => (
              <option key={t.id} value={t.id}>
                {language === 'mr' ? t.nameMr : t.nameEn} ({t.nameEn})
              </option>
            ))}
          </select>
        </div>

        {/* STEP 3: GRAM PANCHAYAT */}
        <div className="space-y-1.5">
          <label className="text-xs font-bold text-stone-700 flex items-center justify-between">
            <span className="flex items-center gap-1.5">
              <span className="w-4 h-4 rounded-full bg-emerald-600 text-white flex items-center justify-center text-[10px]">
                ३
              </span>
              <Landmark className="w-3.5 h-3.5 text-emerald-600" />
              <span>{language === 'mr' ? 'ग्रामपंचायत (GP) *' : '3. Gram Panchayat *'}</span>
            </span>
            {isLoadingGps && <Loader2 className="w-3 h-3 text-emerald-600 animate-spin" />}
            {!isLoadingGps && selectedGpId && (
              <span className="text-[10px] text-stone-400 font-mono">
                LGD: {normalizedGpId}
              </span>
            )}
          </label>

          {allowSearch && loadedGps.length > 0 && (
            <div className="relative">
              <Search className="w-3 h-3 absolute left-2.5 top-1/2 -translate-y-1/2 text-stone-400" />
              <input
                type="text"
                value={gpSearch}
                onChange={(e) => setGpSearch(e.target.value)}
                placeholder={language === 'mr' ? 'ग्रामपंचायत शोधा...' : 'Search GP...'}
                className="w-full pl-7 pr-2 py-1 text-xs rounded-lg border border-stone-200 bg-white placeholder:text-stone-400 focus:outline-none focus:border-emerald-500"
              />
            </div>
          )}

          <select
            value={normalizedGpId}
            onChange={(e) => handleGpChange(e.target.value)}
            disabled={disabled || !normalizedTalId || loadedGps.length === 0 || isLoadingGps}
            className="w-full px-3 py-2 text-xs sm:text-sm rounded-xl border border-stone-300 bg-white font-medium text-stone-900 focus:outline-none focus:border-emerald-500 shadow-xs disabled:bg-stone-100 disabled:text-stone-400"
          >
            <option value="">
              {!normalizedTalId
                ? language === 'mr'
                  ? '-- आधी तालुका निवडा --'
                  : '-- First Select Taluka --'
                : isLoadingGps
                ? language === 'mr'
                  ? 'लोड होत आहे...'
                  : 'Loading GPs...'
                : loadedGps.length === 0
                ? language === 'mr'
                  ? '-- ग्रामपंचायत उपलब्ध नाही --'
                  : '-- No GP Found --'
                : language === 'mr'
                ? `-- ग्रामपंचायत निवडा (${loadedGps.length}) --`
                : `-- Select GP (${loadedGps.length}) --`}
            </option>
            {filteredGps.map((gp) => (
              <option key={gp.id} value={gp.id}>
                {language === 'mr' ? gp.nameMr : gp.nameEn} ({gp.nameEn})
              </option>
            ))}
          </select>
        </div>

        {/* STEP 4: VILLAGE (MAPPED ACCORDING TO OFFICIAL LGD DATA) */}
        {showVillageDropdown && (
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-stone-700 flex items-center justify-between">
              <span className="flex items-center gap-1.5">
                <span className="w-4 h-4 rounded-full bg-blue-600 text-white flex items-center justify-center text-[10px]">
                  ४
                </span>
                <Home className="w-3.5 h-3.5 text-blue-600" />
                <span>{language === 'mr' ? 'महसुली गाव (Village) *' : '4. Mapped Village *'}</span>
              </span>
              {selectedVillageId && (
                <span className="text-[10px] text-stone-400 font-mono">
                  LGD: {selectedVillageId}
                </span>
              )}
            </label>

            {allowSearch && availableVillages.length > 0 && (
              <div className="relative">
                <Search className="w-3 h-3 absolute left-2.5 top-1/2 -translate-y-1/2 text-stone-400" />
                <input
                  type="text"
                  value={villageSearch}
                  onChange={(e) => setVillageSearch(e.target.value)}
                  placeholder={language === 'mr' ? 'गाव शोधा...' : 'Search village...'}
                  className="w-full pl-7 pr-2 py-1 text-xs rounded-lg border border-stone-200 bg-white placeholder:text-stone-400 focus:outline-none focus:border-blue-500"
                />
              </div>
            )}

            <select
              value={selectedVillageId}
              onChange={(e) => handleVillageChange(e.target.value)}
              disabled={disabled || !normalizedGpId || availableVillages.length === 0}
              className="w-full px-3 py-2 text-xs sm:text-sm rounded-xl border border-stone-300 bg-white font-medium text-stone-900 focus:outline-none focus:border-blue-500 shadow-xs disabled:bg-stone-100 disabled:text-stone-400"
            >
              <option value="">
                {!normalizedGpId
                  ? language === 'mr'
                    ? '-- आधी ग्रामपंचायत निवडा --'
                    : '-- First Select GP --'
                  : availableVillages.length === 0
                  ? language === 'mr'
                    ? '-- गाव उपलब्ध नाही --'
                    : '-- No Mapped Village --'
                  : language === 'mr'
                  ? `-- गाव निवडा (${availableVillages.length}) --`
                  : `-- Select Village (${availableVillages.length}) --`}
              </option>
              {filteredVillages.map((v) => (
                <option key={v.id} value={v.id}>
                  {language === 'mr' ? v.nameMr : v.nameEn} ({v.nameEn}) [LGD: {v.code}]
                </option>
              ))}
            </select>
          </div>
        )}
      </div>

      {/* Selected Confirmation badge */}
      {normalizedGpId && (
        <div className="flex flex-wrap items-center justify-between gap-2 pt-1 text-[11px] text-emerald-800 bg-emerald-50/80 px-3 py-2 rounded-xl border border-emerald-200">
          <div className="flex items-center gap-2">
            <Check className="w-3.5 h-3.5 text-emerald-600 shrink-0" />
            <span className="font-medium">
              {language === 'mr' ? 'अधिकृत LGD कार्यक्षेत्र:' : 'Official LGD Jurisdiction:'}{' '}
              <strong className="text-emerald-950">
                {getDistrictById(normalizedDistId)?.nameMr || normalizedDistId} →{' '}
                {getTalukaById(normalizedTalId)?.nameMr || normalizedTalId} →{' '}
                {getGramPanchayatById(normalizedGpId)?.nameMr ||
                  loadedGps.find((g) => g.id === normalizedGpId)?.nameMr ||
                  normalizedGpId}
                {selectedVillageId && (
                  <span>
                    {' '}
                    →{' '}
                    {availableVillages.find((v) => v.id === selectedVillageId)?.nameMr ||
                      selectedVillageId}
                  </span>
                )}
              </strong>
            </span>
          </div>
          <span className="text-[10px] text-emerald-700 font-mono bg-white px-2 py-0.5 rounded-md border border-emerald-200">
            LGD GP #{normalizedGpId}
          </span>
        </div>
      )}
    </div>
  );
};
