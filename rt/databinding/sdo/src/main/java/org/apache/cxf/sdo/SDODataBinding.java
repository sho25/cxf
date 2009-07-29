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
name|sdo
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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Method
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|AbstractDataBinding
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
name|databinding
operator|.
name|WrapperCapableDatabinding
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
name|WrapperHelper
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
name|resource
operator|.
name|ExtendedURIResolver
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
name|Service
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
name|DescriptionInfo
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
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tuscany
operator|.
name|sdo
operator|.
name|api
operator|.
name|SDOUtil
import|;
end_import

begin_import
import|import
name|commonj
operator|.
name|sdo
operator|.
name|DataObject
import|;
end_import

begin_import
import|import
name|commonj
operator|.
name|sdo
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|commonj
operator|.
name|sdo
operator|.
name|helper
operator|.
name|HelperContext
import|;
end_import

begin_import
import|import
name|commonj
operator|.
name|sdo
operator|.
name|impl
operator|.
name|HelperProvider
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SDODataBinding
extends|extends
name|AbstractDataBinding
implements|implements
name|WrapperCapableDatabinding
block|{
specifier|private
specifier|final
class|class
name|SDOWrapperHelper
implements|implements
name|WrapperHelper
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|partNames
decl_stmt|;
specifier|private
name|Method
name|fact
decl_stmt|;
specifier|private
name|Object
name|factory
decl_stmt|;
specifier|private
name|QName
name|wrapperName
decl_stmt|;
specifier|private
name|SDOWrapperHelper
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|partNames
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
parameter_list|,
name|QName
name|wrapperName
parameter_list|)
block|{
name|this
operator|.
name|partNames
operator|=
name|partNames
expr_stmt|;
if|if
condition|(
name|DataObject
operator|.
name|class
operator|!=
name|wrapperType
condition|)
block|{
try|try
block|{
name|String
name|s
init|=
name|wrapperType
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".SdoFactory"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|Class
operator|.
name|forName
argument_list|(
name|s
argument_list|,
literal|false
argument_list|,
name|wrapperType
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|cls
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|==
name|wrapperType
condition|)
block|{
name|fact
operator|=
name|m
expr_stmt|;
break|break;
block|}
block|}
name|factory
operator|=
name|cls
operator|.
name|getField
argument_list|(
literal|"INSTANCE"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|this
operator|.
name|wrapperName
operator|=
name|wrapperName
expr_stmt|;
block|}
specifier|public
name|Object
name|createWrapperObject
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|lst
parameter_list|)
throws|throws
name|Fault
block|{
name|DataObject
name|o
decl_stmt|;
if|if
condition|(
name|fact
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|o
operator|=
operator|(
name|DataObject
operator|)
name|fact
operator|.
name|invoke
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|o
operator|=
name|context
operator|.
name|getDataFactory
argument_list|()
operator|.
name|create
argument_list|(
name|wrapperName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|wrapperName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|lst
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|o
operator|.
name|set
argument_list|(
name|partNames
operator|.
name|get
argument_list|(
name|x
argument_list|)
argument_list|,
name|lst
operator|.
name|get
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|o
return|;
block|}
specifier|public
name|String
name|getSignature
parameter_list|()
block|{
return|return
literal|""
operator|+
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getWrapperParts
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Fault
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|lst
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|DataObject
name|obj
init|=
operator|(
name|DataObject
operator|)
name|o
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|partNames
control|)
block|{
name|lst
operator|.
name|add
argument_list|(
name|obj
operator|.
name|get
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|lst
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|SUPPORTED_READER_FORMATS
index|[]
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMLStreamReader
operator|.
name|class
block|}
empty_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|SUPPORTED_WRITER_FORMATS
index|[]
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMLStreamWriter
operator|.
name|class
operator|,
name|Node
operator|.
name|class
block|}
empty_stmt|;
name|HelperProvider
name|provider
decl_stmt|;
name|HelperContext
name|context
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
name|context
operator|=
name|SDOUtil
operator|.
name|createHelperContext
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|pkgs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceInfo
name|serviceInfo
range|:
name|service
operator|.
name|getServiceInfos
argument_list|()
control|)
block|{
name|SDOClassCollector
name|cc
init|=
operator|new
name|SDOClassCollector
argument_list|(
name|serviceInfo
argument_list|)
decl_stmt|;
name|cc
operator|.
name|walk
argument_list|()
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|cc
operator|.
name|getClasses
argument_list|()
control|)
block|{
if|if
condition|(
name|DataObject
operator|.
name|class
operator|==
name|cls
condition|)
block|{
continue|continue;
block|}
name|String
name|pkg
init|=
name|cls
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pkgs
operator|.
name|contains
argument_list|(
name|pkg
argument_list|)
condition|)
block|{
try|try
block|{
name|Class
name|fact
init|=
name|Class
operator|.
name|forName
argument_list|(
name|pkg
operator|+
literal|".SdoFactory"
argument_list|,
literal|false
argument_list|,
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|registerFactory
argument_list|(
name|fact
argument_list|)
expr_stmt|;
name|pkgs
operator|.
name|add
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore, register the class itself
name|register
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|ServiceInfo
name|serviceInfo
range|:
name|service
operator|.
name|getServiceInfos
argument_list|()
control|)
block|{
name|DescriptionInfo
name|dInfo
init|=
name|serviceInfo
operator|.
name|getDescription
argument_list|()
decl_stmt|;
if|if
condition|(
name|dInfo
operator|!=
literal|null
condition|)
block|{
name|String
name|uri
init|=
name|dInfo
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
name|ExtendedURIResolver
name|resolver
init|=
operator|new
name|ExtendedURIResolver
argument_list|()
decl_stmt|;
name|InputStream
name|ins
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|uri
argument_list|,
literal|""
argument_list|)
operator|.
name|getByteStream
argument_list|()
decl_stmt|;
name|context
operator|.
name|getXSDHelper
argument_list|()
operator|.
name|define
argument_list|(
name|ins
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|void
name|registerFactory
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
parameter_list|)
throws|throws
name|Exception
block|{
name|Field
name|field
init|=
name|factoryClass
operator|.
name|getField
argument_list|(
literal|"INSTANCE"
argument_list|)
decl_stmt|;
name|Object
name|factory
init|=
name|field
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"register"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|HelperContext
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|factory
argument_list|,
operator|new
name|Object
index|[]
block|{
name|context
block|}
argument_list|)
expr_stmt|;
block|}
name|boolean
name|register
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
if|if
condition|(
name|javaType
operator|==
literal|null
operator|||
name|DataObject
operator|.
name|class
operator|==
name|javaType
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|Type
name|type
init|=
name|context
operator|.
name|getTypeHelper
argument_list|()
operator|.
name|getType
argument_list|(
name|javaType
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|type
operator|.
name|isDataType
argument_list|()
operator|)
condition|)
block|{
name|Method
name|method
init|=
name|type
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getEPackage"
argument_list|)
decl_stmt|;
name|Object
name|factory
init|=
name|method
operator|.
name|invoke
argument_list|(
name|type
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
decl_stmt|;
name|method
operator|=
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"register"
argument_list|,
name|HelperContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|factory
argument_list|,
operator|new
name|Object
index|[]
block|{
name|context
block|}
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataReader
argument_list|<
name|T
argument_list|>
name|createReader
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
name|DataReader
argument_list|<
name|T
argument_list|>
name|dr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|==
name|XMLStreamReader
operator|.
name|class
condition|)
block|{
name|dr
operator|=
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|DataReaderImpl
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|dr
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataWriter
argument_list|<
name|T
argument_list|>
name|createWriter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
name|XMLStreamWriter
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|DataWriterImpl
argument_list|(
name|context
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|Node
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|NodeDataWriterImpl
argument_list|(
name|context
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedReaderFormats
parameter_list|()
block|{
return|return
name|SUPPORTED_READER_FORMATS
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedWriterFormats
parameter_list|()
block|{
return|return
name|SUPPORTED_WRITER_FORMATS
return|;
block|}
specifier|public
name|WrapperHelper
name|createWrapperHelper
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
parameter_list|,
specifier|final
name|QName
name|wrapperName
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|partNames
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|elTypeNames
parameter_list|,
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|partClasses
parameter_list|)
block|{
return|return
operator|new
name|SDOWrapperHelper
argument_list|(
name|partNames
argument_list|,
name|wrapperType
argument_list|,
name|wrapperName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

