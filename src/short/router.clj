(ns short.router
  (:require [muuntaja.core :as m]
            [reitit.coercion.malli :as coercion-malli]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.cookies :as cookies]
            [short.middlewares :as middlewares]
            [short.users.routes :as users]
            [short.products.routes :as products]
            [short.variants.routes :as variants]
            [short.shared.s3 :as s3]))

(defn router-config [_environment]
  {:data {:coercion coercion-malli/coercion
          :exception pretty/exception
          :muuntaja m/instance
          :middleware [cookies/wrap-cookies
                       muuntaja/format-middleware
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
                     :version "0.1.0"}
                    :securityDefinitions {:bearer {:type "apiKey"
                                                   :name "token"
                                                   :in "cookies"}}}
                                                   ;; :scheme "bearer"
                                                   ;; :bearerFormat "JWT"}}}
          :handler (swagger/create-swagger-handler)}}])

(defn api-router [environment]
  [swagger-docs
   ["/v1" {:middleware [swagger/swagger-feature]}
    (users/routes environment)
    (products/routes environment)
    (variants/routes environment)
    (s3/routes environment)]])

(def assets-router
  ["" {:no-doc true}
   ["/assets/*" (ring/create-resource-handler {:root "assets/"})]])

(defn router [environment]
  (wrap-cors
   (ring/ring-handler
    (ring/router
     [""
      (api-router environment)
      assets-router]
     (router-config environment))
    (ring/routes
     (swagger-ui/create-swagger-ui-handler {:path "/swagger"}))
    (ring/create-default-handler))
   :access-control-allow-origin [#".*"]
   :access-control-allow-methods [:get :put :post :delete]
   :access-control-allow-credentials "true"))

(defn routes [environment]
  (router environment))
