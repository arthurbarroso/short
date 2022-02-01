(ns short.shared.ui.toast
  (:require ["react-toastify" :refer (ToastContainer toast)]
            [reagent.core :as r]))

(defn toast-wrapper []
  [:<>
   [:> ToastContainer]
   (into [:<>]
         (r/children (r/current-component)))])

(defn show-toast [content]
  (toast content))

(defn success-toast [content]
  (.success toast content))

(defn failure-toast [content]
  (.error toast content))

(defn warn-toast [content]
  (.warn toast content))
