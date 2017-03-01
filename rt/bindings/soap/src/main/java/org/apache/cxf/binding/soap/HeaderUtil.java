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
package|;
end_package

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
name|HashSet
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|wsdl
operator|.
name|extensions
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
name|helpers
operator|.
name|CastUtils
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
name|BindingMessageInfo
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
name|BindingOperationInfo
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
name|MessagePartInfo
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|HeaderUtil
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HEADERS_PROPERTY
init|=
name|HeaderUtil
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".HEADERS"
decl_stmt|;
specifier|private
name|HeaderUtil
parameter_list|()
block|{     }
specifier|private
specifier|static
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaderParts
parameter_list|(
name|BindingMessageInfo
name|bmi
parameter_list|)
block|{
name|Object
name|obj
init|=
name|bmi
operator|.
name|getProperty
argument_list|(
name|HEADERS_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|set
init|=
name|getHeaderQNames
argument_list|(
name|bmi
argument_list|)
decl_stmt|;
name|bmi
operator|.
name|setProperty
argument_list|(
name|HEADERS_PROPERTY
argument_list|,
name|set
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|obj
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaderQNames
parameter_list|(
name|BindingMessageInfo
name|bmi
parameter_list|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|mps
init|=
name|bmi
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extList
init|=
name|bmi
operator|.
name|getExtensors
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|extList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ExtensibilityElement
name|ext
range|:
name|extList
control|)
block|{
if|if
condition|(
name|SOAPBindingUtil
operator|.
name|isSOAPHeader
argument_list|(
name|ext
argument_list|)
condition|)
block|{
name|SoapHeader
name|header
init|=
name|SOAPBindingUtil
operator|.
name|getSoapHeader
argument_list|(
name|ext
argument_list|)
decl_stmt|;
name|String
name|pn
init|=
name|header
operator|.
name|getPart
argument_list|()
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|mps
control|)
block|{
if|if
condition|(
name|pn
operator|.
name|equals
argument_list|(
name|mpi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|mpi
operator|.
name|isElement
argument_list|()
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|mpi
operator|.
name|getElementQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
operator|.
name|add
argument_list|(
name|mpi
operator|.
name|getTypeQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
block|}
block|}
return|return
name|set
return|;
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaderQNameInOperationParam
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bop
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getHeaderParts
argument_list|(
name|bop
operator|.
name|getInput
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|bop
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getHeaderParts
argument_list|(
name|bop
operator|.
name|getOutput
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

