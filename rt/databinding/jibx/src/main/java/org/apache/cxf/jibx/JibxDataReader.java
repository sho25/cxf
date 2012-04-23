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
name|jibx
package|;
end_package

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
name|XMLStreamReader
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
name|jibx
operator|.
name|runtime
operator|.
name|BindingDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|IBindingFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|JiBXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|impl
operator|.
name|StAXReaderWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|impl
operator|.
name|UnmarshallingContext
import|;
end_import

begin_class
specifier|public
class|class
name|JibxDataReader
implements|implements
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
block|{
specifier|public
name|Object
name|read
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamReader
name|input
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|part
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
try|try
block|{
name|UnmarshallingContext
name|ctx
init|=
name|getUnmarshallingContext
argument_list|(
name|input
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|JibxSimpleTypes
operator|.
name|isSimpleType
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|QName
name|stype
init|=
name|part
operator|.
name|getTypeQName
argument_list|()
decl_stmt|;
name|QName
name|ctype
init|=
name|part
operator|.
name|getConcreteName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|.
name|isAt
argument_list|(
name|ctype
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|ctype
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|text
init|=
name|ctx
operator|.
name|parseElementText
argument_list|(
name|ctype
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|ctype
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|JibxSimpleTypes
operator|.
name|toObject
argument_list|(
name|text
argument_list|,
name|stype
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing required element ["
operator|+
name|ctype
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|ctx
operator|.
name|unmarshalElement
argument_list|(
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|JiBXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|QName
name|elementQName
parameter_list|,
name|XMLStreamReader
name|input
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
try|try
block|{
name|UnmarshallingContext
name|ctx
init|=
name|getUnmarshallingContext
argument_list|(
name|input
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
name|ctx
operator|.
name|unmarshalElement
argument_list|(
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JiBXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
block|{     }
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
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{     }
specifier|private
specifier|static
name|UnmarshallingContext
name|getUnmarshallingContext
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jtype
parameter_list|)
throws|throws
name|JiBXException
block|{
name|IBindingFactory
name|factory
decl_stmt|;
if|if
condition|(
name|JibxSimpleTypes
operator|.
name|isSimpleType
argument_list|(
name|jtype
argument_list|)
condition|)
block|{
name|factory
operator|=
name|JibxNullBindingFactory
operator|.
name|getFactory
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
name|BindingDirectory
operator|.
name|getFactory
argument_list|(
name|jtype
argument_list|)
expr_stmt|;
block|}
name|UnmarshallingContext
name|ctx
init|=
operator|(
name|UnmarshallingContext
operator|)
name|factory
operator|.
name|createUnmarshallingContext
argument_list|()
decl_stmt|;
name|StAXReaderWrapper
name|wrapper
init|=
operator|new
name|StAXReaderWrapper
argument_list|(
name|reader
argument_list|,
literal|"Data-element"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|setDocument
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|toTag
argument_list|()
expr_stmt|;
return|return
name|ctx
return|;
block|}
block|}
end_class

end_unit

