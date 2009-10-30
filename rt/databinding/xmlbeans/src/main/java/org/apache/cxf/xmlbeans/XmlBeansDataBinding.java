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
name|xmlbeans
package|;
end_package

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
name|common
operator|.
name|xmlschema
operator|.
name|SchemaCollection
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
name|AbstractWrapperHelper
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
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|XmlBeansDataBinding
extends|extends
name|AbstractDataBinding
implements|implements
name|WrapperCapableDatabinding
block|{
specifier|public
specifier|static
specifier|final
name|String
name|XMLBEANS_NAMESPACE_HACK
init|=
name|XmlBeansDataBinding
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".NamespaceHack"
decl_stmt|;
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
name|XmlBeansDataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|()
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
argument_list|()
return|;
block|}
return|return
literal|null
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
argument_list|()
expr_stmt|;
block|}
return|return
name|dr
return|;
block|}
comment|/**      * XmlBeans has no declared namespace prefixes.      * {@inheritDoc}      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDeclaredNamespaceMappings
parameter_list|()
block|{
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
name|void
name|initialize
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINER
argument_list|,
literal|"Creating XmlBeansDatabinding for "
operator|+
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
name|SchemaCollection
name|col
init|=
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|col
operator|.
name|getXmlSchemas
argument_list|()
operator|.
name|length
operator|>
literal|1
condition|)
block|{
comment|// someone has already filled in the types
continue|continue;
block|}
name|XmlBeansSchemaInitializer
name|schemaInit
init|=
operator|new
name|XmlBeansSchemaInitializer
argument_list|(
name|serviceInfo
argument_list|,
name|col
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|schemaInit
operator|.
name|walk
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|WrapperHelper
name|createWrapperHelper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
parameter_list|,
name|QName
name|wrapperName
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|partNames
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|elTypeNames
parameter_list|,
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
name|List
argument_list|<
name|Method
argument_list|>
name|getMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|(
name|partNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Method
argument_list|>
name|setMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|(
name|partNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|>
argument_list|(
name|partNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Method
name|allMethods
index|[]
init|=
name|wrapperType
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|partNames
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|String
name|partName
init|=
name|partNames
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|partName
operator|==
literal|null
condition|)
block|{
name|getMethods
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setMethods
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|String
name|getAccessor
init|=
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|partName
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|GETTER
argument_list|)
decl_stmt|;
name|String
name|setAccessor
init|=
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|partName
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|SETTER
argument_list|)
decl_stmt|;
name|Method
name|getMethod
init|=
literal|null
decl_stmt|;
name|Method
name|setMethod
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|valueClass
init|=
name|XmlBeansWrapperHelper
operator|.
name|getXMLBeansValueType
argument_list|(
name|wrapperType
argument_list|)
decl_stmt|;
name|allMethods
operator|=
name|valueClass
operator|.
name|getMethods
argument_list|()
expr_stmt|;
try|try
block|{
name|getMethod
operator|=
name|valueClass
operator|.
name|getMethod
argument_list|(
name|getAccessor
argument_list|,
name|AbstractWrapperHelper
operator|.
name|NO_CLASSES
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|//ignore for now
block|}
for|for
control|(
name|Method
name|method
range|:
name|allMethods
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|!=
literal|null
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
operator|(
name|setAccessor
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|setMethod
operator|=
name|method
expr_stmt|;
break|break;
block|}
block|}
name|getMethods
operator|.
name|add
argument_list|(
name|getMethod
argument_list|)
expr_stmt|;
name|setMethods
operator|.
name|add
argument_list|(
name|setMethod
argument_list|)
expr_stmt|;
comment|// There is no filed in the XMLBeans type class
name|fields
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|XmlBeansWrapperHelper
argument_list|(
name|wrapperType
argument_list|,
name|setMethods
operator|.
name|toArray
argument_list|(
operator|new
name|Method
index|[
name|setMethods
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|getMethods
operator|.
name|toArray
argument_list|(
operator|new
name|Method
index|[
name|getMethods
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|fields
operator|.
name|toArray
argument_list|(
operator|new
name|Field
index|[
name|fields
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

