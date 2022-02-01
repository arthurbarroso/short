(ns short.variants.ui.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::variant-form-values
 (fn [db]
   (get-in db [:forms :variant-form])))
