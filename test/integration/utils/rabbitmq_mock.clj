(ns utils.rabbitmq-mock
  (:require [clojure.test :refer :all]
            [langohr.exchange :as le]
            [schema.core :as s]
            [langohr.core :as rmq])
  (:import (clojure.lang PersistentQueue)
           (com.stuartsierra.component Lifecycle)))

; salvar os eventos postados em um atom para cada exchange
; vincular um status ao evento para seber se ja foi processado
; Permitir publicar o evento na fila
