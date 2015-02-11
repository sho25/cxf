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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|Bus
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|addressing
operator|.
name|AttributedURIType
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
name|addressing
operator|.
name|EndpointReferenceType
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
name|addressing
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractObservable
implements|implements
name|Observable
block|{
specifier|protected
name|MessageObserver
name|incomingObserver
decl_stmt|;
comment|/**      * Register a message observer for incoming messages.      *       * @param observer the observer to notify on receipt of incoming      * message      */
specifier|public
specifier|synchronized
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
if|if
condition|(
name|observer
operator|!=
name|incomingObserver
condition|)
block|{
name|MessageObserver
name|old
init|=
name|incomingObserver
decl_stmt|;
comment|// the observer switch must take place before activation or after deactivation
if|if
condition|(
name|observer
operator|!=
literal|null
condition|)
block|{
name|incomingObserver
operator|=
name|observer
expr_stmt|;
specifier|final
name|Logger
name|logger
init|=
name|getLogger
argument_list|()
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"registering incoming observer: "
operator|+
name|observer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|old
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|activate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|incomingObserver
operator|=
literal|null
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|old
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Logger
name|logger
init|=
name|getLogger
argument_list|()
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"unregistering incoming observer: "
operator|+
name|old
argument_list|)
expr_stmt|;
block|}
name|deactivate
argument_list|()
expr_stmt|;
block|}
name|incomingObserver
operator|=
name|observer
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @return the observer to notify on receipt of incoming message      */
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|incomingObserver
return|;
block|}
comment|/**      * Get the target reference .      *       * @param ei the corresponding EndpointInfo      * @return the actual target      */
specifier|protected
specifier|static
name|EndpointReferenceType
name|getTargetReference
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
return|return
name|getTargetReference
argument_list|(
name|ei
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|)
return|;
block|}
comment|/**      * Get the target endpoint reference.      *       * @param ei the corresponding EndpointInfo      * @param t the given target EPR if available      * @param bus the Bus      * @return the actual target      */
specifier|protected
specifier|static
name|EndpointReferenceType
name|getTargetReference
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|t
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|EndpointReferenceType
name|ref
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|t
condition|)
block|{
name|ref
operator|=
operator|new
name|EndpointReferenceType
argument_list|()
expr_stmt|;
name|AttributedURIType
name|address
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|address
operator|.
name|setValue
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|ei
operator|.
name|getService
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|EndpointReferenceUtils
operator|.
name|setServiceAndPortName
argument_list|(
name|ref
argument_list|,
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ei
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ref
operator|=
name|t
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
comment|/**      * Activate messages flow.      */
specifier|protected
name|void
name|activate
parameter_list|()
block|{
comment|// nothing to do by default
block|}
comment|/**      * Deactivate messages flow.      */
specifier|protected
name|void
name|deactivate
parameter_list|()
block|{
comment|// nothing to do by default
block|}
comment|/**      * @return the logger to use      */
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
block|}
end_class

end_unit

