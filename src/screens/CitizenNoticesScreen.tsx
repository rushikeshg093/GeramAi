import React, { useState } from 'react';
import { AppLanguage, Notice } from '../types';
import { Bell, Calendar, Tag, AlertCircle, FileText } from 'lucide-react';

interface CitizenNoticesScreenProps {
  language: AppLanguage;
  notices: Notice[];
}

export const CitizenNoticesScreen: React.FC<CitizenNoticesScreenProps> = ({
  language,
  notices
}) => {
  const [filter, setFilter] = useState<'ALL' | Notice['category']>('ALL');

  const filteredNotices = filter === 'ALL' ? notices : notices.filter((n) => n.category === filter);

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-stone-900">
          {language === 'mr' ? 'ग्रामपंचायत डिजिटल नोटीस बोर्ड' : 'Digital Notice Board'}
        </h1>
        <p className="text-xs text-stone-500 mt-1">
          {language === 'mr'
            ? 'पळसखेड दौलत ग्रामसभा, शासकीय परिपत्रके व महत्त्वाच्या घोषणा'
            : 'Gram Sabha announcements, water alerts and government circulars'}
        </p>
      </div>

      {/* Category Pills */}
      <div className="flex items-center gap-2 overflow-x-auto pb-4 mb-4 text-xs font-bold">
        <button
          onClick={() => setFilter('ALL')}
          className={`px-3.5 py-1.5 rounded-full transition whitespace-nowrap ${
            filter === 'ALL'
              ? 'bg-orange-600 text-white shadow-xs'
              : 'bg-white border border-stone-200 text-stone-600 hover:bg-stone-50'
          }`}
        >
          {language === 'mr' ? 'सर्व सूचना' : 'All'}
        </button>
        <button
          onClick={() => setFilter('GRAMSABHA')}
          className={`px-3.5 py-1.5 rounded-full transition whitespace-nowrap ${
            filter === 'GRAMSABHA'
              ? 'bg-orange-600 text-white shadow-xs'
              : 'bg-white border border-stone-200 text-stone-600 hover:bg-stone-50'
          }`}
        >
          {language === 'mr' ? 'ग्रामसभा' : 'Gram Sabha'}
        </button>
        <button
          onClick={() => setFilter('WATER')}
          className={`px-3.5 py-1.5 rounded-full transition whitespace-nowrap ${
            filter === 'WATER'
              ? 'bg-orange-600 text-white shadow-xs'
              : 'bg-white border border-stone-200 text-stone-600 hover:bg-stone-50'
          }`}
        >
          {language === 'mr' ? 'पाणीपुरवठा' : 'Water'}
        </button>
        <button
          onClick={() => setFilter('TAX')}
          className={`px-3.5 py-1.5 rounded-full transition whitespace-nowrap ${
            filter === 'TAX'
              ? 'bg-orange-600 text-white shadow-xs'
              : 'bg-white border border-stone-200 text-stone-600 hover:bg-stone-50'
          }`}
        >
          {language === 'mr' ? 'कर व योजना' : 'Tax & Schemes'}
        </button>
      </div>

      {/* Notices List */}
      <div className="space-y-4">
        {filteredNotices.map((notice) => (
          <div
            key={notice.id}
            className={`p-6 rounded-2xl border transition bg-white shadow-xs ${
              notice.isImportant ? 'border-amber-300 ring-1 ring-amber-200' : 'border-stone-200'
            }`}
          >
            <div className="flex flex-wrap items-center justify-between gap-2 mb-2">
              <div className="flex items-center gap-2">
                <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-orange-100 text-orange-800">
                  {notice.category}
                </span>
                {notice.isImportant && (
                  <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-amber-500 text-white">
                    {language === 'mr' ? 'महत्त्वाचे' : 'Important'}
                  </span>
                )}
              </div>
              <span className="text-xs text-stone-400 flex items-center gap-1">
                <Calendar className="w-3.5 h-3.5" />
                <span>{notice.publishedDate}</span>
              </span>
            </div>

            <h2 className="text-base sm:text-lg font-bold text-stone-900 mt-1">
              {notice.titleMr}
            </h2>

            <p className="text-xs sm:text-sm text-stone-700 mt-2 leading-relaxed whitespace-pre-line">
              {notice.contentMr}
            </p>

            {notice.attachmentName && (
              <div className="mt-4 pt-3 border-t border-stone-100 flex items-center justify-between text-xs">
                <span className="text-stone-500 flex items-center gap-1.5 font-medium">
                  <FileText className="w-4 h-4 text-orange-600" />
                  <span>{notice.attachmentName}</span>
                </span>
                <button
                  type="button"
                  onClick={() => alert('परिपत्रक डाऊनलोड झाले: ' + notice.attachmentName)}
                  className="text-orange-600 font-bold hover:underline"
                >
                  {language === 'mr' ? 'डाउनलोड करा' : 'Download'}
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
