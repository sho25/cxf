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
name|jaxrs
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Type
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
import|;
end_import

begin_class
annotation|@
name|Provider
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
class|class
name|DataBindingJSONProvider
extends|extends
name|DataBindingProvider
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|arrayKeys
decl_stmt|;
specifier|private
name|boolean
name|serializeAsArray
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|writeXsiType
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|readXsiType
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|setWriteXsiType
parameter_list|(
name|boolean
name|write
parameter_list|)
block|{
name|writeXsiType
operator|=
name|write
expr_stmt|;
block|}
specifier|public
name|void
name|setReadXsiType
parameter_list|(
name|boolean
name|read
parameter_list|)
block|{
name|readXsiType
operator|=
name|read
expr_stmt|;
block|}
specifier|public
name|void
name|setArrayKeys
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|)
block|{
name|this
operator|.
name|arrayKeys
operator|=
name|keys
expr_stmt|;
block|}
specifier|public
name|void
name|setSerializeAsArray
parameter_list|(
name|boolean
name|asArray
parameter_list|)
block|{
name|this
operator|.
name|serializeAsArray
operator|=
name|asArray
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|setNamespaceMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
name|this
operator|.
name|namespaceMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|XMLStreamWriter
name|createWriter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
name|QName
name|qname
init|=
name|getQName
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|JSONUtils
operator|.
name|createStreamWriter
argument_list|(
name|os
argument_list|,
name|qname
argument_list|,
name|writeXsiType
argument_list|,
name|namespaceMap
argument_list|,
name|serializeAsArray
argument_list|,
name|arrayKeys
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeToWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
name|writer
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
name|super
operator|.
name|writeToWriter
argument_list|(
name|writer
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|XMLStreamReader
name|createReader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
name|getQName
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|JSONUtils
operator|.
name|createStreamReader
argument_list|(
name|is
argument_list|,
name|readXsiType
argument_list|,
name|namespaceMap
argument_list|)
return|;
block|}
specifier|private
name|QName
name|getQName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|QName
name|qname
init|=
name|JAXRSUtils
operator|.
name|getClassQName
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|namespaceMap
operator|.
name|putIfAbsent
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
return|return
name|qname
return|;
block|}
block|}
end_class

end_unit

