(ns short.shared
  (:require [malli.core :as ml]
            [clojure.string :as string]
            #?(:clj [muuntaja.core :as m])))

(def Transaction
  (ml/schema [:map
              [:db-before :any]
              [:db-after :any]
              [:tx-data :any]
              [:tempids :map]]))

(defn tempid->eid
  {:malli/schema [:=> [:cat Transaction] :int]}
  [tx]
  (-> tx :tempids first second))

(defn generate-uuid!
  {:malli/schema [:=> :cat :uuid]}
  []
  #?(:clj (java.util.UUID/randomUUID)
     :cljs (generate-uuid!)))

(defn uuid-from-string
  {:malli/schema [:=> [:cat :string] :uuid]}
  [string-uuid]
  #?(:clj (java.util.UUID/fromString string-uuid)
     :cljs (uuid string-uuid)))

(defn get-current-inst!
  {:malli/schema [:=> :cat inst?]}
  []
  #?(:clj (java.util.Date.)
     :cljs (js/Date.)))

(defn edn->json
  {:malli/schema [:=> [:cat :map] :string]}
  [edn-data]
  #?(:clj (m/encode "application/json" edn-data)
     :cljs
     (->> edn-data
          clj->js
          (.stringify js/JSON)
          js/encodeURIComponent)))

(defn json->edn [json]
  {:malli/schema [:=> [:cat :string] :map]}
  #?(:clj (m/decode "application/json" json)
     :cljs (->> json
                js/decodeURIComponent
                (.parse js/JSON))))

(defn slugify [text]
  (-> text
      (string/lower-case)
      (string/replace #"[^\w]+" "-")))
