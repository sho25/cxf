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
operator|.
name|http
operator|.
name|policy
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|message
operator|.
name|MessageUtils
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
name|policy
operator|.
name|PolicyCalculator
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|ObjectFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ClientPolicyCalculator
implements|implements
name|PolicyCalculator
argument_list|<
name|HTTPClientPolicy
argument_list|>
block|{
comment|/**      * Determines if two HTTPClientPolicy objects are equal. REVISIT: Check if      * this can be replaced by a generated equals method.      *      * @param p1 one client policy      * @param p2 another client policy      * @return true iff the two policies are equal      */
specifier|public
name|boolean
name|equals
parameter_list|(
name|HTTPClientPolicy
name|p1
parameter_list|,
name|HTTPClientPolicy
name|p2
parameter_list|)
block|{
if|if
condition|(
name|p1
operator|==
name|p2
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|&=
operator|(
name|p1
operator|.
name|isAllowChunking
argument_list|()
operator|==
name|p2
operator|.
name|isAllowChunking
argument_list|()
operator|)
operator|&&
operator|(
name|p1
operator|.
name|isAutoRedirect
argument_list|()
operator|==
name|p2
operator|.
name|isAutoRedirect
argument_list|()
operator|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getAccept
argument_list|()
argument_list|,
name|p2
operator|.
name|getAccept
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getBrowserType
argument_list|()
argument_list|,
name|p2
operator|.
name|getBrowserType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
return|return
literal|false
return|;
block|}
name|result
operator|&=
operator|(
name|p1
operator|.
name|getCacheControl
argument_list|()
operator|==
literal|null
condition|?
name|p2
operator|.
name|getCacheControl
argument_list|()
operator|==
literal|null
else|:
name|p1
operator|.
name|getCacheControl
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getCacheControl
argument_list|()
argument_list|)
operator|&&
name|p1
operator|.
name|getConnection
argument_list|()
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getConnection
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|p1
operator|.
name|getConnectionTimeout
argument_list|()
operator|==
name|p2
operator|.
name|getConnectionTimeout
argument_list|()
operator|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getContentType
argument_list|()
argument_list|,
name|p2
operator|.
name|getContentType
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getCookie
argument_list|()
argument_list|,
name|p2
operator|.
name|getCookie
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|,
name|p2
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getHost
argument_list|()
argument_list|,
name|p2
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
return|return
literal|false
return|;
block|}
name|result
operator|&=
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getProxyServer
argument_list|()
argument_list|,
name|p2
operator|.
name|getProxyServer
argument_list|()
argument_list|)
operator|&&
operator|(
name|p1
operator|.
name|isSetProxyServerPort
argument_list|()
condition|?
name|p1
operator|.
name|getProxyServerPort
argument_list|()
operator|==
name|p2
operator|.
name|getProxyServerPort
argument_list|()
else|:
operator|!
name|p2
operator|.
name|isSetProxyServerPort
argument_list|()
operator|)
operator|&&
name|p1
operator|.
name|getProxyServerType
argument_list|()
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getProxyServerType
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
operator|&&
operator|(
name|p1
operator|.
name|getReceiveTimeout
argument_list|()
operator|==
name|p2
operator|.
name|getReceiveTimeout
argument_list|()
operator|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|p1
operator|.
name|getReferer
argument_list|()
argument_list|,
name|p2
operator|.
name|getReferer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Returns a new HTTPClientPolicy that is compatible with the two specified      * policies or null if no compatible policy can be determined.      *      * @param p1 one policy      * @param p2 another policy      * @return the compatible policy      */
specifier|public
name|HTTPClientPolicy
name|intersect
parameter_list|(
name|HTTPClientPolicy
name|p1
parameter_list|,
name|HTTPClientPolicy
name|p2
parameter_list|)
block|{
comment|// incompatibilities
if|if
condition|(
operator|!
name|compatible
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// ok - compute compatible policy
name|HTTPClientPolicy
name|p
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|p
operator|.
name|setAccept
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getAccept
argument_list|()
argument_list|,
name|p2
operator|.
name|getAccept
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setAcceptEncoding
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setAcceptLanguage
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|p1
operator|.
name|isSetAllowChunking
argument_list|()
condition|)
block|{
name|p
operator|.
name|setAllowChunking
argument_list|(
name|p1
operator|.
name|isAllowChunking
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetAllowChunking
argument_list|()
condition|)
block|{
name|p
operator|.
name|setAllowChunking
argument_list|(
name|p2
operator|.
name|isAllowChunking
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p1
operator|.
name|isSetAutoRedirect
argument_list|()
condition|)
block|{
name|p
operator|.
name|setAutoRedirect
argument_list|(
name|p1
operator|.
name|isAutoRedirect
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetAutoRedirect
argument_list|()
condition|)
block|{
name|p
operator|.
name|setAutoRedirect
argument_list|(
name|p2
operator|.
name|isAutoRedirect
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|setBrowserType
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getBrowserType
argument_list|()
argument_list|,
name|p2
operator|.
name|getBrowserType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|p1
operator|.
name|isSetCacheControl
argument_list|()
condition|)
block|{
name|p
operator|.
name|setCacheControl
argument_list|(
name|p1
operator|.
name|getCacheControl
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetCacheControl
argument_list|()
condition|)
block|{
name|p
operator|.
name|setCacheControl
argument_list|(
name|p2
operator|.
name|getCacheControl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p1
operator|.
name|isSetConnection
argument_list|()
condition|)
block|{
name|p
operator|.
name|setConnection
argument_list|(
name|p1
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetConnection
argument_list|()
condition|)
block|{
name|p
operator|.
name|setConnection
argument_list|(
name|p2
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p1
operator|.
name|isSetContentType
argument_list|()
condition|)
block|{
name|p
operator|.
name|setContentType
argument_list|(
name|p1
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetContentType
argument_list|()
condition|)
block|{
name|p
operator|.
name|setContentType
argument_list|(
name|p2
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|setCookie
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getCookie
argument_list|()
argument_list|,
name|p2
operator|.
name|getCookie
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDecoupledEndpoint
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|,
name|p2
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setHost
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getHost
argument_list|()
argument_list|,
name|p2
operator|.
name|getHost
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProxyServer
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getProxyServer
argument_list|()
argument_list|,
name|p2
operator|.
name|getProxyServer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|p1
operator|.
name|isSetProxyServerPort
argument_list|()
condition|)
block|{
name|p
operator|.
name|setProxyServerPort
argument_list|(
name|p1
operator|.
name|getProxyServerPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetProxyServerPort
argument_list|()
condition|)
block|{
name|p
operator|.
name|setProxyServerPort
argument_list|(
name|p2
operator|.
name|getProxyServerPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p1
operator|.
name|isSetProxyServerType
argument_list|()
condition|)
block|{
name|p
operator|.
name|setProxyServerType
argument_list|(
name|p1
operator|.
name|getProxyServerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetProxyServerType
argument_list|()
condition|)
block|{
name|p
operator|.
name|setProxyServerType
argument_list|(
name|p2
operator|.
name|getProxyServerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|setReferer
argument_list|(
name|StringUtils
operator|.
name|combine
argument_list|(
name|p1
operator|.
name|getReferer
argument_list|()
argument_list|,
name|p2
operator|.
name|getReferer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|p1
operator|.
name|isSetConnectionTimeout
argument_list|()
condition|)
block|{
name|p
operator|.
name|setConnectionTimeout
argument_list|(
name|p1
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetConnectionTimeout
argument_list|()
condition|)
block|{
name|p
operator|.
name|setConnectionTimeout
argument_list|(
name|p2
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p1
operator|.
name|isSetReceiveTimeout
argument_list|()
condition|)
block|{
name|p
operator|.
name|setReceiveTimeout
argument_list|(
name|p1
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|.
name|isSetReceiveTimeout
argument_list|()
condition|)
block|{
name|p
operator|.
name|setReceiveTimeout
argument_list|(
name|p2
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|p
return|;
block|}
comment|/**      * Checks if two HTTPClientPolicy objects are compatible.      *      * @param p1 one client policy      * @param p2 another client policy      * @return true iff policies are compatible      */
specifier|public
name|boolean
name|compatible
parameter_list|(
name|HTTPClientPolicy
name|p1
parameter_list|,
name|HTTPClientPolicy
name|p2
parameter_list|)
block|{
if|if
condition|(
name|p1
operator|==
name|p2
operator|||
name|p1
operator|.
name|equals
argument_list|(
name|p2
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|compatible
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getAccept
argument_list|()
argument_list|,
name|p2
operator|.
name|getAccept
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|,
name|p2
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getBrowserType
argument_list|()
argument_list|,
name|p2
operator|.
name|getBrowserType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
operator|!
name|p1
operator|.
name|isSetCacheControl
argument_list|()
operator|||
operator|!
name|p2
operator|.
name|isSetCacheControl
argument_list|()
operator|||
name|p1
operator|.
name|getCacheControl
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getCacheControl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|=
operator|!
name|p1
operator|.
name|isSetConnection
argument_list|()
operator|||
operator|!
name|p2
operator|.
name|isSetConnection
argument_list|()
operator|||
name|p1
operator|.
name|getConnection
argument_list|()
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getConnection
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|=
operator|!
name|p1
operator|.
name|isSetContentType
argument_list|()
operator|||
operator|!
name|p2
operator|.
name|isSetContentType
argument_list|()
operator|||
name|p1
operator|.
name|getContentType
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getCookie
argument_list|()
argument_list|,
name|p2
operator|.
name|getCookie
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// REVISIT: Should compatibility require strict equality?
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|,
name|p2
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getHost
argument_list|()
argument_list|,
name|p2
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getProxyServer
argument_list|()
argument_list|,
name|p2
operator|.
name|getProxyServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
operator|!
name|p1
operator|.
name|isSetProxyServerPort
argument_list|()
operator|||
operator|!
name|p2
operator|.
name|isSetProxyServerPort
argument_list|()
operator|||
name|p1
operator|.
name|getProxyServerPort
argument_list|()
operator|==
name|p2
operator|.
name|getProxyServerPort
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
operator|!
name|p1
operator|.
name|isSetProxyServerType
argument_list|()
operator|||
operator|!
name|p2
operator|.
name|isSetProxyServerType
argument_list|()
operator|||
name|p1
operator|.
name|getProxyServerType
argument_list|()
operator|.
name|equals
argument_list|(
name|p2
operator|.
name|getProxyServerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|StringUtils
operator|.
name|compatible
argument_list|(
name|p1
operator|.
name|getReferer
argument_list|()
argument_list|,
name|p2
operator|.
name|getReferer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|p1
operator|.
name|isAllowChunking
argument_list|()
operator|==
name|p2
operator|.
name|isAllowChunking
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|compatible
condition|)
block|{
name|compatible
operator|&=
name|p1
operator|.
name|isAutoRedirect
argument_list|()
operator|==
name|p2
operator|.
name|isAutoRedirect
argument_list|()
expr_stmt|;
block|}
return|return
name|compatible
return|;
block|}
specifier|public
name|boolean
name|isAsserted
parameter_list|(
name|Message
name|message
parameter_list|,
name|HTTPClientPolicy
name|policy
parameter_list|,
name|HTTPClientPolicy
name|refPolicy
parameter_list|)
block|{
name|boolean
name|outbound
init|=
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|boolean
name|compatible
init|=
name|compatible
argument_list|(
name|policy
argument_list|,
name|refPolicy
argument_list|)
decl_stmt|;
return|return
operator|!
name|outbound
operator|||
name|compatible
return|;
block|}
specifier|public
name|Class
argument_list|<
name|HTTPClientPolicy
argument_list|>
name|getDataClass
parameter_list|()
block|{
return|return
name|HTTPClientPolicy
operator|.
name|class
return|;
block|}
specifier|public
name|QName
name|getDataClassName
parameter_list|()
block|{
return|return
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createClient
argument_list|(
literal|null
argument_list|)
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|HTTPClientPolicy
name|p
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"[DecoupledEndpoint=\""
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|p
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\", ReceiveTimeout="
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|p
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"])"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

