(ns short.s3
  (:require [amazonica.aws.s3 :as s3-client]))

(defn generate-upload-url [{:keys [env config file-key]}]
  (let [creds (:s3 env)]
    (s3-client/generate-presigned-url creds (:bucket config) file-key "POST")))

(comment
  (let [creds {:access-key ""
               :secret-key ""
               :endpoint ""}
        presigned (s3-client/generate-presigned-url creds "" "key-teste"
                                                    (java.util.Date.))]
    (clojure.pprint/pprint presigned)))
