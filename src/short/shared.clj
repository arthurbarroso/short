(ns short.shared
  (:require [muuntaja.core :as m]))

(defn tempid->eid [tx]
  (-> tx :tempids first second))

(defn generate-uuid! []
  (java.util.UUID/randomUUID))

(defn get-current-inst! []
  (java.util.Date.))

(defn edn->json [edn-data]
  (m/encode "application/json" edn-data))
