(ns short.s3.routes
  (:require [short.s3.controllers :as co]
            [short.middlewares :as mi]
            [short.s3.contracts :as c]))

(defn routes [environment]
  ["/s3"
   ["/generate"
    {:middleware [[mi/jws-middleware environment]]
     :post {:handler (co/generate-upload-url-controller! environment)
            :parameters {:body c/S3PresignedUrlData}
            :swagger {:security [{:bearer []}]}}}]])
