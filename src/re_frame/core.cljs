(ns re-frame.core
  (:require [re-frame.v041 :as v041]
            [re-frame.middleware :as middleware]))

; this file provides public API to default re-frame setup
; note: by including this namespace, you will get default app-db, default app-frame and default event channel
; plus it will start default router-loop automatically

; * app-db is backed by reagent/atom
; * app-frame has default loggers
; * router event channel is implemented using core.async channel

; the default instance of app-db
(def app-db (v041/make-app-db-atom))

; the default instance of re-frame
(def app-frame (v041/make-frame-atom))

; the default event queue
(def event-chan (v041/make-event-chan))

; --  API  ----------------------------------------------------------------------------------------------------------

(def set-loggers! (partial v041/set-loggers! app-frame))
(def register-sub (partial v041/register-sub app-frame))
(def unregister-sub (partial v041/unregister-sub app-frame))
(def clear-sub-handlers! (partial v041/clear-sub-handlers! app-frame))
(def subscribe (partial v041/subscribe app-frame app-db))
(def clear-event-handlers! (partial v041/clear-event-handlers! app-frame))
(def register-handler
  "register a handler for an event.
  This is low level and it is expected that \"re-frame.core/register-handler\" would
  generally be used."
  (partial v041/register-handler app-frame))
(def unregister-handler (partial v041/unregister-handler app-frame))

(def dispatch
  "Send an event to be processed by the registered handler.

Usage example:
   (dispatch [:delete-item 42])
"
  (partial v041/dispatch event-chan app-frame))

(def dispatch-sync
  "Send an event to be processed by the registered handler, but avoid the async-inducing
use of core.async/chan.

Usage example:
   (dispatch-sync [:delete-item 42])"
  (partial v041/dispatch-sync app-db app-frame))

; --  middleware  ---------------------------------------------------------------------------------------------------

(def debug (middleware/debug app-frame))
(def path (middleware/path app-frame))
(def enrich (middleware/enrich app-frame))
(def trim-v (middleware/trim-v app-frame))
(def after (middleware/after app-frame))
(def log-ex (middleware/log-ex app-frame))
(def on-changes (middleware/on-changes app-frame))

(def run-router-loop (partial v041/run-router-loop event-chan app-db app-frame))

; --  event processing  ---------------------------------------------------------------------------------------------

(run-router-loop)
