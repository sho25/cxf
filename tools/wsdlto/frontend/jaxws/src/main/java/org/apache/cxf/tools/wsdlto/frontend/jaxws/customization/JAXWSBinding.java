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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
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
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSBinding
implements|implements
name|ExtensibilityElement
implements|,
name|Serializable
block|{
specifier|private
name|boolean
name|enableAsyncMapping
decl_stmt|;
specifier|private
name|boolean
name|enableMime
decl_stmt|;
specifier|private
name|Element
name|element
decl_stmt|;
specifier|private
name|boolean
name|required
decl_stmt|;
specifier|private
name|QName
name|elementType
decl_stmt|;
specifier|private
name|boolean
name|enableWrapperStyle
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|documentBaseURI
decl_stmt|;
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|private
name|String
name|methodName
decl_stmt|;
specifier|private
name|JAXWSParameter
name|jaxwsPara
decl_stmt|;
specifier|private
name|JAXWSClass
name|jaxwsClass
decl_stmt|;
specifier|public
name|void
name|setDocumentBaseURI
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|documentBaseURI
operator|=
name|baseURI
expr_stmt|;
block|}
specifier|public
name|String
name|getDocumentBaseURI
parameter_list|()
block|{
return|return
name|this
operator|.
name|documentBaseURI
return|;
block|}
specifier|public
name|void
name|setElement
parameter_list|(
name|Element
name|elem
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|Element
name|getElement
parameter_list|()
block|{
return|return
name|element
return|;
block|}
specifier|public
name|void
name|setRequired
parameter_list|(
name|Boolean
name|r
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|r
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
specifier|public
name|void
name|setElementType
parameter_list|(
name|QName
name|elemType
parameter_list|)
block|{
name|this
operator|.
name|elementType
operator|=
name|elemType
expr_stmt|;
block|}
specifier|public
name|QName
name|getElementType
parameter_list|()
block|{
return|return
name|elementType
return|;
block|}
specifier|public
name|boolean
name|isEnableMime
parameter_list|()
block|{
return|return
name|enableMime
return|;
block|}
specifier|public
name|void
name|setEnableMime
parameter_list|(
name|boolean
name|enableMime
parameter_list|)
block|{
name|this
operator|.
name|enableMime
operator|=
name|enableMime
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnableAsyncMapping
parameter_list|()
block|{
return|return
name|this
operator|.
name|enableAsyncMapping
return|;
block|}
specifier|public
name|void
name|setEnableAsyncMapping
parameter_list|(
name|boolean
name|enableAsync
parameter_list|)
block|{
name|this
operator|.
name|enableAsyncMapping
operator|=
name|enableAsync
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnableWrapperStyle
parameter_list|()
block|{
return|return
name|enableWrapperStyle
return|;
block|}
specifier|public
name|void
name|setEnableWrapperStyle
parameter_list|(
name|boolean
name|pEnableWrapperStyle
parameter_list|)
block|{
name|this
operator|.
name|enableWrapperStyle
operator|=
name|pEnableWrapperStyle
expr_stmt|;
block|}
specifier|public
name|void
name|setPackage
parameter_list|(
name|String
name|pkg
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|pkg
expr_stmt|;
block|}
specifier|public
name|String
name|getPackage
parameter_list|()
block|{
return|return
name|this
operator|.
name|packageName
return|;
block|}
specifier|public
name|void
name|setJaxwsPara
parameter_list|(
name|JAXWSParameter
name|para
parameter_list|)
block|{
name|jaxwsPara
operator|=
name|para
expr_stmt|;
block|}
specifier|public
name|JAXWSParameter
name|getJaxwsPara
parameter_list|()
block|{
return|return
name|jaxwsPara
return|;
block|}
specifier|public
name|void
name|setJaxwsClass
parameter_list|(
name|JAXWSClass
name|clz
parameter_list|)
block|{
name|this
operator|.
name|jaxwsClass
operator|=
name|clz
expr_stmt|;
block|}
specifier|public
name|JAXWSClass
name|getJaxwsClass
parameter_list|()
block|{
return|return
name|this
operator|.
name|jaxwsClass
return|;
block|}
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|methodName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|this
operator|.
name|methodName
return|;
block|}
block|}
end_class

end_unit

