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
name|Unmarshaller
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
name|databinding
operator|.
name|DataReader
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_class
specifier|public
class|class
name|DataReaderImpl
parameter_list|<
name|T
parameter_list|>
extends|extends
name|JAXBDataBase
implements|implements
name|DataReader
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
name|JAXBDataBinding
name|databinding
decl_stmt|;
name|boolean
name|unwrapJAXBElement
init|=
literal|true
decl_stmt|;
specifier|public
name|DataReaderImpl
parameter_list|(
name|JAXBDataBinding
name|binding
parameter_list|,
name|boolean
name|unwrap
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
name|unwrapJAXBElement
operator|=
name|unwrap
expr_stmt|;
name|databinding
operator|=
name|binding
expr_stmt|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|T
name|input
parameter_list|)
block|{
return|return
name|read
argument_list|(
literal|null
argument_list|,
name|input
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|prop
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|prop
operator|.
name|equals
argument_list|(
name|JAXBDataBinding
operator|.
name|UNWRAP_JAXB_ELEMENT
argument_list|)
condition|)
block|{
name|unwrapJAXBElement
operator|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Unmarshaller
name|createUnmarshaller
parameter_list|()
block|{
try|try
block|{
name|Unmarshaller
name|um
init|=
literal|null
decl_stmt|;
name|um
operator|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
expr_stmt|;
if|if
condition|(
name|databinding
operator|.
name|getUnmarshallerListener
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|um
operator|.
name|setListener
argument_list|(
name|databinding
operator|.
name|getUnmarshallerListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|um
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
if|if
condition|(
name|databinding
operator|.
name|getUnmarshallerProperties
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
name|getUnmarshallerProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
block|{
name|um
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
name|um
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|um
operator|.
name|setAttachmentUnmarshaller
argument_list|(
name|getAttachmentUnmarshaller
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|um
return|;
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
name|UnmarshalException
condition|)
block|{
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|UnmarshalException
name|unmarshalEx
init|=
operator|(
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|UnmarshalException
operator|)
name|ex
decl_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"UNMARSHAL_ERROR"
argument_list|,
name|LOG
argument_list|,
name|unmarshalEx
operator|.
name|getLinkedException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
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
literal|"UNMARSHAL_ERROR"
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
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|T
name|reader
parameter_list|)
block|{
name|boolean
name|honorJaxbAnnotation
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
operator|&&
name|part
operator|.
name|getProperty
argument_list|(
literal|"honor.jaxb.annotations"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|honorJaxbAnnotation
operator|=
operator|(
name|Boolean
operator|)
name|part
operator|.
name|getProperty
argument_list|(
literal|"honor.jaxb.annotations"
argument_list|)
expr_stmt|;
block|}
name|Annotation
index|[]
name|anns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|honorJaxbAnnotation
condition|)
block|{
name|anns
operator|=
name|getJAXBAnnotation
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|anns
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|//RpcLit will use the JAXB Bridge to unmarshall part message when it is
comment|//annotated with @XmlList,@XmlAttachmentRef,@XmlJavaTypeAdapter
comment|//TODO:Cache the JAXBRIContext
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|null
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|JAXBEncoderDecoder
operator|.
name|unmarshalWithBridge
argument_list|(
name|qname
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
name|reader
argument_list|,
name|getAttachmentUnmarshaller
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|JAXBEncoderDecoder
operator|.
name|unmarshall
argument_list|(
name|createUnmarshaller
argument_list|()
argument_list|,
name|reader
argument_list|,
name|part
argument_list|,
name|unwrapJAXBElement
argument_list|)
return|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|QName
name|name
parameter_list|,
name|T
name|input
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
return|return
name|JAXBEncoderDecoder
operator|.
name|unmarshall
argument_list|(
name|createUnmarshaller
argument_list|()
argument_list|,
name|input
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|unwrapJAXBElement
argument_list|)
return|;
block|}
block|}
end_class

end_unit

