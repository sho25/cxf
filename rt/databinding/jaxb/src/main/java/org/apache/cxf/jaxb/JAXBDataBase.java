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
name|ArrayList
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
name|List
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
name|JAXBContext
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
name|annotation
operator|.
name|XmlAttachmentRef
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
name|annotation
operator|.
name|XmlList
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
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|attachment
operator|.
name|AttachmentUnmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jaxb
operator|.
name|attachment
operator|.
name|JAXBAttachmentUnmarshaller
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
name|Attachment
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
name|service
operator|.
name|model
operator|.
name|AbstractMessageContainer
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
name|MessageInfo
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
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|JAXBDataBase
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXBDataBase
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|JAXBContext
name|context
decl_stmt|;
specifier|protected
name|Schema
name|schema
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
specifier|protected
name|Integer
name|mtomThreshold
decl_stmt|;
comment|// null if we should default.
specifier|protected
name|JAXBDataBase
parameter_list|(
name|JAXBContext
name|ctx
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setJAXBContext
parameter_list|(
name|JAXBContext
name|jc
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|jc
expr_stmt|;
block|}
specifier|public
name|Schema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|JAXBContext
name|getJAXBContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
specifier|protected
name|AttachmentUnmarshaller
name|getAttachmentUnmarshaller
parameter_list|()
block|{
return|return
operator|new
name|JAXBAttachmentUnmarshaller
argument_list|(
name|attachments
argument_list|)
return|;
block|}
specifier|protected
name|AttachmentMarshaller
name|getAttachmentMarshaller
parameter_list|()
block|{
return|return
operator|new
name|JAXBAttachmentMarshaller
argument_list|(
name|attachments
argument_list|,
name|mtomThreshold
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
block|{     }
specifier|protected
name|Annotation
index|[]
name|getJAXBAnnotation
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|List
argument_list|<
name|Annotation
argument_list|>
name|annoList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mpi
operator|!=
literal|null
condition|)
block|{
name|annoList
operator|=
name|extractJAXBAnnotations
argument_list|(
operator|(
name|Annotation
index|[]
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
literal|"parameter.annotations"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|annoList
operator|==
literal|null
condition|)
block|{
name|annoList
operator|=
name|extractJAXBAnnotations
argument_list|(
name|getReturnMethodAnnotations
argument_list|(
name|mpi
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|annoList
operator|==
literal|null
condition|?
operator|new
name|Annotation
index|[
literal|0
index|]
else|:
name|annoList
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|Annotation
argument_list|>
name|extractJAXBAnnotations
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|List
argument_list|<
name|Annotation
argument_list|>
name|annoList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|anns
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|anns
control|)
block|{
if|if
condition|(
name|ann
operator|instanceof
name|XmlList
operator|||
name|ann
operator|instanceof
name|XmlAttachmentRef
operator|||
name|ann
operator|instanceof
name|XmlJavaTypeAdapter
condition|)
block|{
if|if
condition|(
name|annoList
operator|==
literal|null
condition|)
block|{
name|annoList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|annoList
operator|.
name|add
argument_list|(
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|annoList
return|;
block|}
specifier|private
name|Annotation
index|[]
name|getReturnMethodAnnotations
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|AbstractMessageContainer
name|mi
init|=
name|mpi
operator|.
name|getMessageInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|mi
operator|==
literal|null
operator|||
operator|!
name|isOutputMessage
argument_list|(
name|mi
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|OperationInfo
name|oi
init|=
name|mi
operator|!=
literal|null
condition|?
name|mi
operator|.
name|getOperation
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|oi
operator|!=
literal|null
condition|?
operator|(
name|Annotation
index|[]
operator|)
name|oi
operator|.
name|getProperty
argument_list|(
literal|"method.return.annotations"
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|boolean
name|isOutputMessage
parameter_list|(
name|AbstractMessageContainer
name|messageContainer
parameter_list|)
block|{
if|if
condition|(
name|messageContainer
operator|instanceof
name|MessageInfo
condition|)
block|{
return|return
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|MessageInfo
operator|)
name|messageContainer
operator|)
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|Integer
name|getMtomThreshold
parameter_list|()
block|{
return|return
name|mtomThreshold
return|;
block|}
specifier|public
name|void
name|setMtomThreshold
parameter_list|(
name|Integer
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|mtomThreshold
operator|=
name|threshold
expr_stmt|;
block|}
specifier|protected
specifier|final
name|boolean
name|honorJAXBAnnotations
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
if|if
condition|(
name|part
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
comment|//RPC-Lit always needs to look for these
return|return
literal|true
return|;
block|}
comment|//certain cases that use XmlJavaTypeAdapters will require this and the
comment|//JAXBSchemaInitializer will set this.
name|Boolean
name|b
init|=
operator|(
name|Boolean
operator|)
name|part
operator|.
name|getProperty
argument_list|(
literal|"honor.jaxb.annotations"
argument_list|)
decl_stmt|;
return|return
name|b
operator|==
literal|null
condition|?
literal|false
else|:
name|b
return|;
block|}
specifier|protected
name|ValidationEventHandler
name|getValidationEventHandler
parameter_list|(
name|String
name|cn
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|ValidationEventHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|cn
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|ClassNotFoundException
name|e
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
literal|"Could not create validation event handler"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ValidationEventHandler
name|getValidationEventHandler
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|Object
name|value
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
name|ValidationEventHandler
name|veventHandler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|veventHandler
operator|=
name|getValidationEventHandler
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|veventHandler
operator|=
operator|(
name|ValidationEventHandler
operator|)
name|value
expr_stmt|;
block|}
if|if
condition|(
name|veventHandler
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|JAXBDataBinding
operator|.
name|VALIDATION_EVENT_HANDLER
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|veventHandler
operator|=
name|getValidationEventHandler
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|veventHandler
operator|=
operator|(
name|ValidationEventHandler
operator|)
name|value
expr_stmt|;
block|}
block|}
return|return
name|veventHandler
return|;
block|}
block|}
end_class

end_unit

