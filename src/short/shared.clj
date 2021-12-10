(ns short.shared)

(defn tempid->eid [tx]
  (-> tx :tempids first second))

(defn generate-uuid! []
  (java.util.UUID/randomUUID))

(defn get-current-inst! []
  (java.util.Date.))
