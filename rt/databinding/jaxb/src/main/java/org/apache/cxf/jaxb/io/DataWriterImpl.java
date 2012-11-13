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
name|jaxb
operator|.
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|Marshaller
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
name|PropertyException
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
name|ValidationEvent
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
name|ValidationEventHandler
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
name|attachment
operator|.
name|AttachmentMarshaller
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
name|jaxb
operator|.
name|JAXBUtils
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
name|databinding
operator|.
name|DataWriter
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
name|jaxb
operator|.
name|JAXBDataBase
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|jaxb
operator|.
name|JAXBEncoderDecoder
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
name|jaxb
operator|.
name|attachment
operator|.
name|JAXBAttachmentMarshaller
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_class
specifier|public
class|class
name|DataWriterImpl
parameter_list|<
name|T
parameter_list|>
extends|extends
name|JAXBDataBase
implements|implements
name|DataWriter
argument_list|<
name|T
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
name|getLogger
argument_list|(
name|JAXBDataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JAXBDataBinding
name|databinding
decl_stmt|;
specifier|public
name|DataWriterImpl
parameter_list|(
name|JAXBDataBinding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|binding
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|databinding
operator|=
name|binding
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|T
name|output
parameter_list|)
block|{
name|write
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|MtomValidationHandler
implements|implements
name|ValidationEventHandler
block|{
name|ValidationEventHandler
name|origHandler
decl_stmt|;
name|JAXBAttachmentMarshaller
name|marshaller
decl_stmt|;
specifier|public
name|MtomValidationHandler
parameter_list|(
name|ValidationEventHandler
name|v
parameter_list|,
name|JAXBAttachmentMarshaller
name|m
parameter_list|)
block|{
name|origHandler
operator|=
name|v
expr_stmt|;
name|marshaller
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|boolean
name|handleEvent
parameter_list|(
name|ValidationEvent
name|event
parameter_list|)
block|{
name|String
name|msg
init|=
name|event
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|startsWith
argument_list|(
literal|"cvc-type.3.1.2: "
argument_list|)
operator|&&
name|msg
operator|.
name|contains
argument_list|(
name|marshaller
operator|.
name|getLastMTOMElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|origHandler
operator|!=
literal|null
condition|)
block|{
return|return
name|origHandler
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|Marshaller
name|createMarshaller
parameter_list|(
name|Object
name|elValue
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
name|cls
operator|=
name|part
operator|.
name|getTypeClass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|cls
operator|=
literal|null
operator|!=
name|elValue
condition|?
name|elValue
operator|.
name|getClass
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|!=
literal|null
operator|&&
name|cls
operator|.
name|isArray
argument_list|()
operator|&&
name|elValue
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|col
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|elValue
decl_stmt|;
name|elValue
operator|=
name|col
operator|.
name|toArray
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|cls
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|col
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Marshaller
name|marshaller
decl_stmt|;
try|try
block|{
name|marshaller
operator|=
name|context
operator|.
name|createMarshaller
argument_list|()
expr_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|setListener
argument_list|(
name|databinding
operator|.
name|getMarshallerListener
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|databinding
operator|.
name|getValidationEventHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|marshaller
operator|.
name|setEventHandler
argument_list|(
name|databinding
operator|.
name|getValidationEventHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspref
init|=
name|databinding
operator|.
name|getDeclaredNamespaceMappings
argument_list|()
decl_stmt|;
if|if
condition|(
name|nspref
operator|!=
literal|null
condition|)
block|{
name|JAXBUtils
operator|.
name|setNamespaceWrapper
argument_list|(
name|nspref
argument_list|,
name|marshaller
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|databinding
operator|.
name|getMarshallerProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propEntry
range|:
name|databinding
operator|.
name|getMarshallerProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
block|{
name|marshaller
operator|.
name|setProperty
argument_list|(
name|propEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|propEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyException
name|pe
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"PropertyException setting Marshaller properties"
argument_list|,
name|pe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|marshaller
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|AttachmentMarshaller
name|atmarsh
init|=
name|getAttachmentMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setAttachmentMarshaller
argument_list|(
name|atmarsh
argument_list|)
expr_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
operator|&&
name|atmarsh
operator|instanceof
name|JAXBAttachmentMarshaller
condition|)
block|{
comment|//we need a special even handler for XOP attachments
name|marshaller
operator|.
name|setEventHandler
argument_list|(
operator|new
name|MtomValidationHandler
argument_list|(
name|marshaller
operator|.
name|getEventHandler
argument_list|()
argument_list|,
operator|(
name|JAXBAttachmentMarshaller
operator|)
name|atmarsh
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|MarshalException
condition|)
block|{
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|MarshalException
name|marshalEx
init|=
operator|(
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|MarshalException
operator|)
name|ex
decl_stmt|;
name|Message
name|faultMessage
init|=
operator|new
name|Message
argument_list|(
literal|"MARSHAL_ERROR"
argument_list|,
name|LOG
argument_list|,
name|marshalEx
operator|.
name|getLinkedException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|faultMessage
argument_list|,
name|ex
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"MARSHAL_ERROR"
argument_list|,
name|LOG
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|marshaller
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|T
name|output
parameter_list|)
block|{
name|boolean
name|honorJaxbAnnotation
init|=
name|honorJAXBAnnotations
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
operator|&&
operator|!
name|part
operator|.
name|isElement
argument_list|()
operator|&&
name|part
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|honorJaxbAnnotation
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
operator|||
operator|!
operator|(
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
operator|)
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Exception
operator|&&
name|part
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getProperty
argument_list|(
name|JAXBDataBinding
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".CUSTOM_EXCEPTION"
argument_list|)
argument_list|)
condition|)
block|{
name|JAXBEncoderDecoder
operator|.
name|marshallException
argument_list|(
name|createMarshaller
argument_list|(
name|obj
argument_list|,
name|part
argument_list|)
argument_list|,
operator|(
name|Exception
operator|)
name|obj
argument_list|,
name|part
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Annotation
index|[]
name|anns
init|=
name|getJAXBAnnotation
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|honorJaxbAnnotation
operator|||
name|anns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|JAXBEncoderDecoder
operator|.
name|marshall
argument_list|(
name|createMarshaller
argument_list|(
name|obj
argument_list|,
name|part
argument_list|)
argument_list|,
name|obj
argument_list|,
name|part
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|honorJaxbAnnotation
operator|&&
name|anns
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|//RpcLit will use the JAXB Bridge to marshall part message when it is
comment|//annotated with @XmlList,@XmlAttachmentRef,@XmlJavaTypeAdapter
comment|//TODO:Cache the JAXBRIContext
name|JAXBEncoderDecoder
operator|.
name|marshalWithBridge
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|anns
argument_list|,
name|databinding
operator|.
name|getContextClasses
argument_list|()
argument_list|,
name|obj
argument_list|,
name|output
argument_list|,
name|getAttachmentMarshaller
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|needToRender
argument_list|(
name|part
argument_list|)
condition|)
block|{
name|JAXBEncoderDecoder
operator|.
name|marshallNullElement
argument_list|(
name|createMarshaller
argument_list|(
literal|null
argument_list|,
name|part
argument_list|)
argument_list|,
name|output
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|needToRender
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
if|if
condition|(
name|part
operator|!=
literal|null
operator|&&
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|XmlSchemaElement
name|element
init|=
operator|(
name|XmlSchemaElement
operator|)
name|part
operator|.
name|getXmlSchema
argument_list|()
decl_stmt|;
return|return
name|element
operator|.
name|isNillable
argument_list|()
operator|&&
name|element
operator|.
name|getMinOccurs
argument_list|()
operator|>
literal|0
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

