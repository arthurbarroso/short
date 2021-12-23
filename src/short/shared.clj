(ns short.shared
  (:require [muuntaja.core :as m]))

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
  (java.util.UUID/randomUUID))

(defn uuid-from-string [string]
  (java.util.UUID/fromString string))

(defn get-current-inst! []
  (java.util.Date.))

(defn edn->json [edn-data]
  (m/encode "application/json" edn-data))
