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
name|transfer
operator|.
name|manager
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|ws
operator|.
name|WebServiceContext
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|ReferenceParametersType
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
name|transfer
operator|.
name|Representation
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
name|transfer
operator|.
name|shared
operator|.
name|faults
operator|.
name|UnknownResource
import|;
end_import

begin_comment
comment|/**  * In memory implementation for ResourceManager interface.  */
end_comment

begin_class
specifier|public
class|class
name|MemoryResourceManager
implements|implements
name|ResourceManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REF_NAMESPACE
init|=
literal|"http://cxf.apache.org/rt/ws/transfer/MemoryResourceManager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REF_LOCAL_NAME
init|=
literal|"uuid"
decl_stmt|;
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
name|MemoryResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|storage
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|MemoryResourceManager
parameter_list|()
block|{
name|storage
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Representation
name|get
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|)
block|{
name|String
name|uuid
init|=
name|getUUID
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|storage
operator|.
name|containsKey
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnknownResource
argument_list|()
throw|;
block|}
name|String
name|resource
init|=
name|storage
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|Representation
argument_list|()
return|;
block|}
else|else
block|{
name|Document
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StringReader
argument_list|(
name|storage
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"Internal Error"
argument_list|,
name|getSoapVersion
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|)
throw|;
block|}
name|Representation
name|representation
init|=
operator|new
name|Representation
argument_list|()
decl_stmt|;
name|representation
operator|.
name|setAny
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|representation
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|delete
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|)
block|{
name|String
name|uuid
init|=
name|getUUID
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|storage
operator|.
name|containsKey
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnknownResource
argument_list|()
throw|;
block|}
name|storage
operator|.
name|remove
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|put
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|,
name|Representation
name|newRepresentation
parameter_list|)
block|{
name|String
name|uuid
init|=
name|getUUID
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|storage
operator|.
name|containsKey
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnknownResource
argument_list|()
throw|;
block|}
name|Element
name|representationEl
init|=
operator|(
name|Element
operator|)
name|newRepresentation
operator|.
name|getAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|representationEl
operator|==
literal|null
condition|)
block|{
name|storage
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|storage
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
name|StaxUtils
operator|.
name|toString
argument_list|(
name|representationEl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ReferenceParametersType
name|create
parameter_list|(
name|Representation
name|initRepresentation
parameter_list|)
block|{
comment|// Store xmlResource
name|String
name|uuid
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Element
name|representationEl
init|=
operator|(
name|Element
operator|)
name|initRepresentation
operator|.
name|getAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|representationEl
operator|==
literal|null
condition|)
block|{
name|storage
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|storage
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
name|StaxUtils
operator|.
name|toString
argument_list|(
name|representationEl
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Element
name|uuidEl
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|REF_NAMESPACE
argument_list|,
name|REF_LOCAL_NAME
argument_list|)
decl_stmt|;
name|uuidEl
operator|.
name|setTextContent
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
comment|// Create referenceParameter
name|ReferenceParametersType
name|refParam
init|=
operator|new
name|ReferenceParametersType
argument_list|()
decl_stmt|;
name|refParam
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|uuidEl
argument_list|)
expr_stmt|;
return|return
name|refParam
return|;
block|}
specifier|private
name|String
name|getUUID
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|)
block|{
for|for
control|(
name|Object
name|object
range|:
name|ref
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|element
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|QName
name|qName
init|=
name|element
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|REF_NAMESPACE
operator|.
name|equals
argument_list|(
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|REF_LOCAL_NAME
operator|.
name|equals
argument_list|(
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|element
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|REF_NAMESPACE
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|REF_LOCAL_NAME
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|element
operator|.
name|getTextContent
argument_list|()
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|UnknownResource
argument_list|()
throw|;
block|}
specifier|private
name|SoapVersion
name|getSoapVersion
parameter_list|()
block|{
name|WrappedMessageContext
name|wmc
init|=
operator|(
name|WrappedMessageContext
operator|)
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|SoapMessage
name|message
init|=
operator|(
name|SoapMessage
operator|)
name|wmc
operator|.
name|getWrappedMessage
argument_list|()
decl_stmt|;
return|return
name|message
operator|.
name|getVersion
argument_list|()
return|;
block|}
block|}
end_class

end_unit

