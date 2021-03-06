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
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|xacml2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|helpers
operator|.
name|DOMUtils
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
name|interceptor
operator|.
name|Fault
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
name|interceptor
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|security
operator|.
name|LoginSecurityContext
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|OpenSAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|util
operator|.
name|DOM2Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|DecisionType
operator|.
name|DECISION
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|RequestType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|ResponseType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|ResultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|StatusType
import|;
end_import

begin_comment
comment|/**  * An interceptor to perform an XACML 2.0 authorization request to a remote PDP using OpenSAML,  * and make an authorization decision based on the response. It takes the principal and roles  * from the SecurityContext, and uses the XACMLRequestBuilder to construct an XACML Request  * statement. How the actual PDP invocation is made is up to a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXACMLAuthorizingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractXACMLAuthorizingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|XACMLRequestBuilder
name|requestBuilder
init|=
operator|new
name|DefaultXACMLRequestBuilder
argument_list|()
decl_stmt|;
specifier|public
name|AbstractXACMLAuthorizingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|LoginSecurityContext
condition|)
block|{
name|Principal
name|principal
init|=
name|sc
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
name|String
name|principalName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|principal
operator|!=
literal|null
condition|)
block|{
name|principalName
operator|=
name|principal
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|LoginSecurityContext
name|loginSecurityContext
init|=
operator|(
name|LoginSecurityContext
operator|)
name|sc
decl_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|principalRoles
init|=
name|loginSecurityContext
operator|.
name|getUserRoles
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|principalRoles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Principal
name|p
range|:
name|principalRoles
control|)
block|{
if|if
condition|(
name|p
operator|!=
literal|null
operator|&&
name|p
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|p
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|principalName
argument_list|)
condition|)
block|{
name|roles
operator|.
name|add
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
try|try
block|{
if|if
condition|(
name|authorize
argument_list|(
name|principal
argument_list|,
name|roles
argument_list|,
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Unauthorized: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"Unauthorized"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"The SecurityContext was not an instance of LoginSecurityContext. No authorization "
operator|+
literal|"is possible as a result"
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"Unauthorized"
argument_list|)
throw|;
block|}
specifier|public
name|XACMLRequestBuilder
name|getRequestBuilder
parameter_list|()
block|{
return|return
name|requestBuilder
return|;
block|}
specifier|public
name|void
name|setRequestBuilder
parameter_list|(
name|XACMLRequestBuilder
name|requestBuilder
parameter_list|)
block|{
name|this
operator|.
name|requestBuilder
operator|=
name|requestBuilder
expr_stmt|;
block|}
comment|/**      * Perform a (remote) authorization decision and return a boolean depending on the result      */
specifier|protected
name|boolean
name|authorize
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|RequestType
name|request
init|=
name|requestBuilder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|roles
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|requestElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|request
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|requestElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ResponseType
name|response
init|=
name|performRequest
argument_list|(
name|request
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ResultType
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|ResultType
name|result
range|:
name|results
control|)
block|{
comment|// Handle any Obligations returned by the PDP
name|handleObligations
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|message
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|DECISION
name|decision
init|=
name|result
operator|.
name|getDecision
argument_list|()
operator|!=
literal|null
condition|?
name|result
operator|.
name|getDecision
argument_list|()
operator|.
name|getDecision
argument_list|()
else|:
name|DECISION
operator|.
name|Deny
decl_stmt|;
name|String
name|code
init|=
literal|""
decl_stmt|;
name|String
name|statusMessage
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|getStatus
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StatusType
name|status
init|=
name|result
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|code
operator|=
name|status
operator|.
name|getStatusCode
argument_list|()
operator|!=
literal|null
condition|?
name|status
operator|.
name|getStatusCode
argument_list|()
operator|.
name|getValue
argument_list|()
else|:
literal|""
expr_stmt|;
name|statusMessage
operator|=
name|status
operator|.
name|getStatusMessage
argument_list|()
operator|!=
literal|null
condition|?
name|status
operator|.
name|getStatusMessage
argument_list|()
operator|.
name|getValue
argument_list|()
else|:
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"XACML authorization result: "
operator|+
name|decision
operator|+
literal|", code: "
operator|+
name|code
operator|+
literal|", message: "
operator|+
name|statusMessage
argument_list|)
expr_stmt|;
block|}
return|return
name|decision
operator|==
name|DECISION
operator|.
name|Permit
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Handle any Obligations returned by the PDP      */
specifier|protected
name|void
name|handleObligations
parameter_list|(
name|RequestType
name|request
parameter_list|,
name|Principal
name|principal
parameter_list|,
name|Message
name|message
parameter_list|,
name|ResultType
name|result
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Do nothing by default
block|}
specifier|protected
specifier|abstract
name|ResponseType
name|performRequest
parameter_list|(
name|RequestType
name|request
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

