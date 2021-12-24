(ns short.cookies
  (:require [goog.net.cookies :as cks]
            [cljs.reader :as reader]))

(defn edn->json [value]
  (->> value
       clj->js
       (.stringify js/JSON)
       js/encodeURIComponent))

(defn keywordize [x] (js->clj x :keywordize-keys true))

(defn json->edn [json]
  (->> json
       js/decodeURIComponent
       (.parse js/JSON)))

(defn set-cookie!
  [key value]
  (let [content (edn->json value)]
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
