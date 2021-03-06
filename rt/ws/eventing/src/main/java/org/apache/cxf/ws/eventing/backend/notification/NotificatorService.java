begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|eventing
operator|.
name|backend
operator|.
name|notification
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|eventing
operator|.
name|backend
operator|.
name|database
operator|.
name|SubscriptionTicket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|eventing
operator|.
name|backend
operator|.
name|manager
operator|.
name|SubscriptionManagerInterfaceForNotificators
import|;
end_import

begin_comment
comment|/**  * The service which takes care of notifying subscribers about events. Has access to the subscription database.  * Receives events from compliant Emitters, eg. EmitterServlet / EmitterMBean,..  * Don't forget to use the 'stop' method, especially if running inside a servlet container!!  * Suggested approach for a web container is to instantiate this class in a ServletContextListener  * and then have it stopped using the same listener. If you don't call 'stop' upon undeployment,  * the underlying ExecutorService will not be shut down, leaking resources.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|NotificatorService
block|{
specifier|public
specifier|static
specifier|final
name|int
name|CORE_POOL_SIZE
init|=
literal|15
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|NotificatorService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ExecutorService
name|service
decl_stmt|;
specifier|public
name|NotificatorService
parameter_list|()
block|{     }
specifier|protected
specifier|abstract
name|SubscriptionManagerInterfaceForNotificators
name|obtainManager
parameter_list|()
function_decl|;
specifier|public
name|void
name|dispatchEvent
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"NotificatorService is not started. "
operator|+
literal|"Please call the start() method before passing any events to it."
argument_list|)
throw|;
block|}
for|for
control|(
name|SubscriptionTicket
name|ticket
range|:
name|obtainManager
argument_list|()
operator|.
name|getTickets
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|ticket
operator|.
name|isExpired
argument_list|()
condition|)
block|{
name|submitNotificationTask
argument_list|(
name|ticket
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Ticket expired at "
operator|+
name|ticket
operator|.
name|getExpires
argument_list|()
operator|.
name|toXMLFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|submitNotificationTask
parameter_list|(
name|SubscriptionTicket
name|ticket
parameter_list|,
name|Object
name|event
parameter_list|)
function_decl|;
specifier|public
name|void
name|subscriptionEnd
parameter_list|(
name|SubscriptionTicket
name|ticket
parameter_list|,
name|String
name|reason
parameter_list|,
name|SubscriptionEndStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"NotificatorService will notify about subscription end for ticket="
operator|+
name|ticket
operator|.
name|getUuid
argument_list|()
operator|+
literal|"; reason="
operator|+
name|reason
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
operator|new
name|SubscriptionEndNotificationTask
argument_list|(
name|ticket
argument_list|,
name|reason
argument_list|,
name|status
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts this NotificatorService. You MUST run this method on every instance      * before starting to pass any events to it. Run it only once.      */
specifier|public
name|void
name|start
parameter_list|()
block|{
name|obtainManager
argument_list|()
operator|.
name|registerNotificator
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|service
operator|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
name|CORE_POOL_SIZE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shuts down the NotificatorService. This method is a MUST if you are running it inside a servlet container,      * because it will shutdown the underlying ExecutorService.      */
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

