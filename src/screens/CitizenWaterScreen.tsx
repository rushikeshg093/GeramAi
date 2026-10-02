import React, { useState } from 'react';
import { AppLanguage, CitizenUser, WaterSchedule, TankerBooking } from '../types';
import { Droplets, Clock, CheckCircle, Truck, Send, AlertTriangle } from 'lucide-react';

interface CitizenWaterScreenProps {
  language: AppLanguage;
  citizen: CitizenUser;
  waterSchedules: WaterSchedule[];
}

export const CitizenWaterScreen: React.FC<CitizenWaterScreenProps> = ({
  language,
  citizen,
  waterSchedules
}) => {
  const [tankers, setTankers] = useState<TankerBooking[]>([
    {
      id: 'TNK-2026-081',
      gramPanchayatId: citizen.gramPanchayatId,
      citizenName: citizen.fullName,
      citizenPhone: citizen.mobileNumber,
      wardNumber: citizen.wardNumber,
      deliveryDate: '२०२६-०९-३०',
      purpose: 'घरगुती पिण्याचे पाणी व कौटुंबिक कार्यक्रम',
      status: 'APPROVED',
      requestedAt: '२०२६-०९-२९ १२:३०',
      driverName: 'श्री. अमोल पवार',
      driverPhone: '९४२२००९९८८'
    }
  ]);

  const [deliveryDate, setDeliveryDate] = useState('२०२६-१०-०१');
  const [purpose, setPurpose] = useState('घरगुती पाणीटंचाई');
  const [showTankerModal, setShowTankerModal] = useState(false);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const handleBookTanker = (e: React.FormEvent) => {
    e.preventDefault();
    const newBooking: TankerBooking = {
      id: `TNK-2026-0${tankers.length + 82}`,
      gramPanchayatId: citizen.gramPanchayatId,
      citizenName: citizen.fullName,
      citizenPhone: citizen.mobileNumber,
      wardNumber: citizen.wardNumber,
      deliveryDate,
      purpose,
      status: 'REQUESTED',
      requestedAt: new Date().toLocaleDateString('mr-IN')
    };

    setTankers([newBooking, ...tankers]);
    setShowTankerModal(false);
    setSuccessMsg(
      language === 'mr'
        ? 'टँकर मागणी यशस्वीरीत्या नोंदवली गेली! पाणीपुरवठा अधिकारी लवकरच संपर्क करतील.'
        : 'Tanker request submitted successfully!'
    );
    setTimeout(() => setSuccessMsg(null), 4000);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-stone-900">
            {language === 'mr' ? 'पाणीपुरवठा सेवा व टँकर कक्ष' : 'Water Supply & Tanker Services'}
          </h1>
          <p className="text-xs text-stone-500 mt-1">
            {language === 'mr'
              ? 'पळसखेड दौलत प्रभागनिहाय पाणीपुरवठा वेळापत्रक व मोफत टँकर बुकिंग'
              : 'Ward-wise drinking water schedules and tanker booking'}
          </p>
        </div>

        <button
          onClick={() => setShowTankerModal(true)}
          className="px-4 py-2.5 rounded-xl bg-sky-600 hover:bg-sky-700 text-white text-xs font-bold shadow-xs transition flex items-center gap-1.5 self-start sm:self-auto"
        >
          <Truck className="w-4 h-4" />
          <span>{language === 'mr' ? 'पाण्याचा टँकर बुक करा' : 'Book Water Tanker'}</span>
        </button>
      </div>

      {successMsg && (
        <div className="mb-6 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-xs font-semibold text-emerald-800">
          ✓ {successMsg}
        </div>
      )}

      {/* Ward Schedules Grid */}
      <div className="mb-8">
        <h2 className="text-base font-bold text-stone-900 mb-3 flex items-center gap-2">
          <Droplets className="w-5 h-5 text-sky-600" />
          <span>{language === 'mr' ? 'प्रभागनिहाय पाणीपुरवठा वेळापत्रक' : 'Ward Water Schedules'}</span>
        </h2>

        <div className="grid md:grid-cols-2 gap-4">
          {waterSchedules.map((ws) => (
            <div
              key={ws.id}
              className={`p-5 rounded-2xl border transition ${
                ws.wardNumber === citizen.wardNumber
                  ? 'border-sky-500 bg-sky-50/50 shadow-xs ring-1 ring-sky-500/20'
                  : 'border-stone-200 bg-white'
              }`}
            >
              <div className="flex items-center justify-between mb-3">
                <div className="flex items-center gap-2">
                  <span className="font-bold text-sm text-stone-900">{ws.wardNumber}</span>
                  <span className="text-xs text-stone-500">({ws.wardName})</span>
                  {ws.wardNumber === citizen.wardNumber && (
                    <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-sky-600 text-white">
                      {language === 'mr' ? 'आपला प्रभाग' : 'Your Ward'}
                    </span>
                  )}
                </div>
                <span
                  className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                    ws.status === 'ACTIVE'
                      ? 'bg-emerald-100 text-emerald-800'
                      : ws.status === 'DELAYED'
                      ? 'bg-amber-100 text-amber-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  {ws.status}
                </span>
              </div>

              <div className="space-y-1.5 text-xs text-stone-600 mb-3">
                <div className="flex items-center justify-between">
                  <span>🌅 सकाळची वेळ:</span>
                  <span className="font-bold text-stone-900">{ws.morningTime}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span>🌆 सायंकाळची वेळ:</span>
                  <span className="font-bold text-stone-900">{ws.eveningTime}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span>💧 जलशुद्धीकरण (Chlorination):</span>
                  <span className="font-semibold text-emerald-700">
                    {ws.chlorinationDone ? '✓ पूर्ण झाले आहे' : 'प्रलंबित'}
                  </span>
                </div>
              </div>

              {ws.notes && (
                <div className="p-2.5 rounded-xl bg-amber-50 border border-amber-200 text-xs text-amber-900 leading-relaxed">
                  ℹ️ {ws.notes}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Tanker Bookings History */}
      <div>
        <h2 className="text-base font-bold text-stone-900 mb-3 flex items-center gap-2">
          <Truck className="w-5 h-5 text-stone-700" />
          <span>{language === 'mr' ? 'टँकर मागणी इतिहास' : 'Tanker Bookings History'}</span>
        </h2>

        <div className="space-y-3">
          {tankers.map((t) => (
            <div key={t.id} className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
              <div className="flex items-center justify-between mb-2">
                <span className="font-mono text-xs font-bold text-stone-500">{t.id}</span>
                <span
                  className={`px-2.5 py-0.5 rounded-full text-xs font-bold ${
                    t.status === 'APPROVED'
                      ? 'bg-emerald-100 text-emerald-800'
                      : 'bg-amber-100 text-amber-800'
                  }`}
                >
                  {t.status === 'APPROVED' ? 'मंजूर (Approved)' : 'प्रलंबित (Requested)'}
                </span>
              </div>
              <div className="text-sm font-bold text-stone-900">{t.purpose}</div>
              <div className="text-xs text-stone-500 mt-1">
                प्रभाग: {t.wardNumber} • अपेक्षित तारीख: {t.deliveryDate} • नोंदणी: {t.requestedAt}
              </div>
              {t.driverName && (
                <div className="mt-2 text-xs bg-emerald-50 text-emerald-900 p-2 rounded-lg border border-emerald-200 flex items-center justify-between">
                  <span>चालक: {t.driverName}</span>
                  <a href={`tel:${t.driverPhone}`} className="font-bold underline">
                    {t.driverPhone}
                  </a>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Tanker Modal */}
      {showTankerModal && (
        <div className="fixed inset-0 z-50 bg-black/40 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl border border-stone-200">
            <h3 className="text-lg font-bold text-stone-900 mb-1">
              {language === 'mr' ? 'पाण्याचा टँकर बुक करा' : 'Request Drinking Water Tanker'}
            </h3>
            <p className="text-xs text-stone-500 mb-4">
              {citizen.fullName} • {citizen.wardNumber}
            </p>

            <form onSubmit={handleBookTanker} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'टँकर आवश्यक असणारी तारीख *' : 'Delivery Date *'}
                </label>
                <input
                  type="date"
                  value={deliveryDate}
                  onChange={(e) => setDeliveryDate(e.target.value)}
                  className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-sky-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'पाण्याची गरज / कारण *' : 'Purpose / Reason *'}
                </label>
                <select
                  value={purpose}
                  onChange={(e) => setPurpose(e.target.value)}
                  className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 bg-white"
                >
                  <option value="घरगुती पाणीटंचाई">घरगुती पाणीटंचाई (Domestic Shortage)</option>
                  <option value="कौटुंबिक/धार्मिक कार्यक्रम">कौटुंबिक / धार्मिक कार्यक्रम (Event/Function)</option>
                  <option value="जनावरांसाठी पिण्याचे पाणी">जनावरांसाठी पिण्याचे पाणी (Livestock)</option>
                  <option value="पाईपलाईन दुरुस्तीमुळे खंड">पाईपलाईन दुरुस्तीमुळे खंड (Pipeline issue)</option>
                </select>
              </div>

              <div className="flex justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowTankerModal(false)}
                  className="px-4 py-2 rounded-xl text-xs font-semibold text-stone-600 hover:bg-stone-100"
                >
                  {language === 'mr' ? 'रद्द करा' : 'Cancel'}
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 rounded-xl bg-sky-600 hover:bg-sky-700 text-white text-xs font-bold shadow-xs transition"
                >
                  {language === 'mr' ? 'टँकर नोंदणी करा' : 'Submit Request'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
