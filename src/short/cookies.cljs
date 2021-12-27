(ns short.cookies
  (:require [goog.net.cookies :as cks]
            [short.shared :as shared]))

(defn keywordize [x] (js->clj x :keywordize-keys true))

(defn set-cookie!
  [key value]
  (let [content (shared/edn->json value)]
    (.set goog.net.cookies key content)))

(defn set-raw-cookie!
  [key value]
  (.set goog.net.cookies key value))

(defn get-cookie!
  [key]
  (->> key
       (.get goog.net.cookies)
       js/decodeURIComponent
       (.parse js/JSON)
       keywordize))
