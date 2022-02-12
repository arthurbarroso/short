(ns short.shared.s3
  (:require [amazonica.aws.s3 :as client]
            [ring.util.response :as rr]
            [short.middlewares :as mi]))

(def S3PresignedUrlData
  [:map
   [:file-size number?]
   [:file-key string?]
   [:file-type string?]])

(def S3SignedURL
  [:or
   [:map
    [:s3/url string?]]
   [:map
    [:s3/error string?]]])

(def max-file-size 10000)
(def allowed-file-types ["image/png" "image/jpg" "image/jpeg"])

(defn generate-signed-url [environment file-key]
  (let [{:keys [creds bucket]} (:s3 environment)]
    {:s3/url (client/generate-presigned-url creds {:bucket-name bucket
                                                   :method "PUT"
                                                   :key file-key})}))

(defn generate-upload-url!
  {:malli/schema [:=> [:cat S3PresignedUrlData :any] S3SignedURL]}
  [s3-data environment]
  (let [{:keys [file-type file-size file-key]} s3-data]
    (if (and (< file-size max-file-size) (some #(= file-type %) allowed-file-types))
      (generate-signed-url environment file-key)
      {:s3/error "Invalid file"})))

(defn generate-upload-url-controller! [environment]
  (fn [request]
    (let [file-input (-> request :parameters :body)
          upload-url (generate-upload-url!
                      file-input
                      environment)]
      (if (:s3/url upload-url)
        (rr/response upload-url)
        (rr/bad-request {:error "Something went wrong"})))))

(defn routes [environment]
  ["/s3"
   ["/generate"
    {:middleware [[mi/jws-middleware environment]]
     :post {:handler (generate-upload-url-controller! environment)
            :parameters {:body S3PresignedUrlData}
            :swagger {:security [{:bearer []}]}}}]])
