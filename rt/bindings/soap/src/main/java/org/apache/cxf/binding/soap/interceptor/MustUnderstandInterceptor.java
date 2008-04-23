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
name|binding
operator|.
name|soap
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|Logger
import|;
end_import

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
name|binding
operator|.
name|soap
operator|.
name|HeaderUtil
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|i18n
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
name|headers
operator|.
name|Header
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
name|Interceptor
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

begin_class
specifier|public
class|class
name|MustUnderstandInterceptor
extends|extends
name|AbstractSoapInterceptor
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
name|MustUnderstandInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
specifier|public
name|MustUnderstandInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MustUnderstandInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
name|SoapVersion
name|soapVersion
init|=
name|soapMessage
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Header
argument_list|>
name|mustUnderstandHeaders
init|=
operator|new
name|HashSet
argument_list|<
name|Header
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|URI
argument_list|>
name|serviceRoles
init|=
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|QName
argument_list|>
name|notUnderstandQNames
init|=
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|QName
argument_list|>
name|mustUnderstandQNames
init|=
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
name|buildMustUnderstandHeaders
argument_list|(
name|mustUnderstandHeaders
argument_list|,
name|soapMessage
argument_list|,
name|serviceRoles
argument_list|)
expr_stmt|;
name|initServiceSideInfo
argument_list|(
name|mustUnderstandQNames
argument_list|,
name|soapMessage
argument_list|,
name|serviceRoles
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|checkUnderstand
argument_list|(
name|mustUnderstandHeaders
argument_list|,
name|mustUnderstandQNames
argument_list|,
name|notUnderstandQNames
argument_list|)
condition|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|(
literal|300
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
literal|0
decl_stmt|;
for|for
control|(
name|QName
name|qname
range|:
name|notUnderstandQNames
control|)
block|{
name|pos
operator|=
name|pos
operator|+
name|qname
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|2
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|qname
operator|.
name|toString
argument_list|()
operator|+
literal|", "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|delete
argument_list|(
name|pos
operator|-
literal|2
argument_list|,
name|pos
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"MUST_UNDERSTAND"
argument_list|,
name|BUNDLE
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|soapVersion
operator|.
name|getMustUnderstand
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|initServiceSideInfo
parameter_list|(
name|Set
argument_list|<
name|QName
argument_list|>
name|mustUnderstandQNames
parameter_list|,
name|SoapMessage
name|soapMessage
parameter_list|,
name|Set
argument_list|<
name|URI
argument_list|>
name|serviceRoles
parameter_list|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|paramHeaders
init|=
name|HeaderUtil
operator|.
name|getHeaderQNameInOperationParam
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramHeaders
operator|!=
literal|null
condition|)
block|{
name|mustUnderstandQNames
operator|.
name|addAll
argument_list|(
name|paramHeaders
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Interceptor
name|interceptorInstance
range|:
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
control|)
block|{
if|if
condition|(
name|interceptorInstance
operator|instanceof
name|SoapInterceptor
condition|)
block|{
name|SoapInterceptor
name|si
init|=
operator|(
name|SoapInterceptor
operator|)
name|interceptorInstance
decl_stmt|;
name|Set
argument_list|<
name|URI
argument_list|>
name|roles
init|=
name|si
operator|.
name|getRoles
argument_list|()
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
name|serviceRoles
operator|.
name|addAll
argument_list|(
name|roles
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|QName
argument_list|>
name|understoodHeaders
init|=
name|si
operator|.
name|getUnderstoodHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|understoodHeaders
operator|!=
literal|null
condition|)
block|{
name|mustUnderstandQNames
operator|.
name|addAll
argument_list|(
name|understoodHeaders
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|buildMustUnderstandHeaders
parameter_list|(
name|Set
argument_list|<
name|Header
argument_list|>
name|mustUnderstandHeaders
parameter_list|,
name|SoapMessage
name|soapMessage
parameter_list|,
name|Set
argument_list|<
name|URI
argument_list|>
name|serviceRoles
parameter_list|)
block|{
for|for
control|(
name|Header
name|header
range|:
name|soapMessage
operator|.
name|getHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|header
operator|instanceof
name|SoapHeader
operator|&&
operator|(
operator|(
name|SoapHeader
operator|)
name|header
operator|)
operator|.
name|isMustUnderstand
argument_list|()
condition|)
block|{
name|String
name|role
init|=
operator|(
operator|(
name|SoapHeader
operator|)
name|header
operator|)
operator|.
name|getActor
argument_list|()
decl_stmt|;
if|if
condition|(
name|role
operator|!=
literal|null
condition|)
block|{
name|role
operator|=
name|role
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|role
operator|.
name|equals
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
operator|.
name|getNextRole
argument_list|()
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
operator|.
name|getUltimateReceiverRole
argument_list|()
argument_list|)
condition|)
block|{
name|mustUnderstandHeaders
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|URI
name|roleFromBinding
range|:
name|serviceRoles
control|)
block|{
if|if
condition|(
name|role
operator|.
name|equals
argument_list|(
name|roleFromBinding
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|mustUnderstandHeaders
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
comment|// if role omitted, the soap node is ultimate receiver,
comment|// needs to understand
name|mustUnderstandHeaders
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|checkUnderstand
parameter_list|(
name|Set
argument_list|<
name|Header
argument_list|>
name|mustUnderstandHeaders
parameter_list|,
name|Set
argument_list|<
name|QName
argument_list|>
name|mustUnderstandQNames
parameter_list|,
name|Set
argument_list|<
name|QName
argument_list|>
name|notUnderstandQNames
parameter_list|)
block|{
for|for
control|(
name|Header
name|header
range|:
name|mustUnderstandHeaders
control|)
block|{
name|QName
name|qname
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|mustUnderstandQNames
operator|.
name|contains
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|notUnderstandQNames
operator|.
name|add
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|notUnderstandQNames
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

