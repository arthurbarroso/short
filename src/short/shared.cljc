(ns short.shared
  (:require #?(:clj [muuntaja.core :as m])))

(def Transaction
  [:map
   [:db-before :any]
   [:db-after :any]
   [:tx-data :any]
   [:tempids :map]])

(defn tempid->eid
  {:malli/schema [:=> [:cat Transaction] int?]}
  [tx]
  (-> tx :tempids first second))

(defn generate-uuid! []
  #?(:clj (java.util.UUID/randomUUID)
     :cljs (generate-uuid!)))

(defn uuid-from-string [string]
  #?(:clj (java.util.UUID/fromString string)
     :cljs (uuid string)))

(defn get-current-inst! []
  #?(:clj (java.util.Date.)
     :cljs (js/Date.)))

(defn edn->json [edn-data]
  #?(:clj (m/encode "application/json" edn-data)
     :cljs
     (->> edn-data
          clj->js
          (.stringify js/JSON)
          js/encodeURIComponent)))

(defn json->edn [json]
  #?(:clj (m/decode "application/json" json)
     :cljs (->> json
                js/decodeURIComponent
                (.parse js/JSON))))
