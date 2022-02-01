(ns short.shared.session-storage
  (:require [short.shared :as shared]))

(defn set! [{:keys [data key]}]
  (.setItem (.-sessionStorage js/window)
            key
            (shared/edn->json data)))

(defn get! [key]
  (let [raw-json (.getItem (.-sessionStorage js/window)
                           key)]
    (shared/json->edn raw-json)))
