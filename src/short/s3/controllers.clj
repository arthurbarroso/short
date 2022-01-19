(ns short.s3.controllers
  (:require [short.s3.handlers :as h]
            [ring.util.response :as rr]))

(defn generate-upload-url-controller! [environment]
  (fn [request]
    (let [file-input (-> request :parameters :body)
          upload-url (h/generate-upload-url!
                      file-input
                      environment)]
      (if (:s3/url upload-url)
        (rr/response upload-url)
        (rr/bad-request {:error "Something went wrong"})))))
