(ns short.router
  (:require [muuntaja.core :as m]
            [reitit.coercion.spec :as coercion-spec]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.cors :refer [wrap-cors]]
            [short.middlewares :as middlewares]
            [short.users.routes :as users]))

(def router-config
  {:data {:coercion coercion-spec/coercion
          :exception pretty/exception
          :muuntaja m/instance
          :middleware [muuntaja/format-middleware
                       exception/exception-middleware
                       coercion/coerce-request-middleware
                       coercion/coerce-response-middleware
                       coercion/coerce-exceptions-middleware
                       middlewares/exception-middleware]}})

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info
                    {:title "Brundij API documentation"
                     :description "Brundij's REST api"
                     :version "0.1.0"}}
          :handler (swagger/create-swagger-handler)}}])

(defn api-router [environment]
  [swagger-docs
   ["/v1" {:middleware [swagger/swagger-feature]}
    (users/routes environment)]])

(defn router [environment]
  (wrap-cors
   (ring/ring-handler
    (ring/router
     [""
      (api-router environment)]
     router-config)
    (ring/routes
     (swagger-ui/create-swagger-ui-handler {:path "/swagger"}))
    (ring/create-default-handler))
   :access-control-allow-origin [#".*"]
   :access-control-allow-methods [:get :put :post :delete]))

(defn routes [environment]
  (router environment))
