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
name|Collections
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSoapInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
implements|implements
name|SoapInterceptor
block|{
comment|/**      * @deprecated      */
specifier|public
name|AbstractSoapInterceptor
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractSoapInterceptor
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|super
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractSoapInterceptor
parameter_list|(
name|String
name|i
parameter_list|,
name|String
name|p
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|URI
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getFaultCodePrefix
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|QName
name|faultCode
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|codeNs
init|=
name|faultCode
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|codeNs
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|prefix
operator|=
name|StaxUtils
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|,
name|codeNs
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|prefix
return|;
block|}
block|}
end_class

end_unit

