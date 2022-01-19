(ns short.s3.contracts)

(def S3PresignedUrlData
  [:map
   [:file-size number?]
   [:file-key string?]
   [:file-type string?]])
