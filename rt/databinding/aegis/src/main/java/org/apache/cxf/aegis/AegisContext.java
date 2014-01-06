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
name|aegis
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
name|Array
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
name|GenericArrayType
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
name|ParameterizedType
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
name|HashSet
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
name|XMLStreamException
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
name|Document
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
name|aegis
operator|.
name|type
operator|.
name|AbstractTypeCreator
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|aegis
operator|.
name|type
operator|.
name|DefaultTypeCreator
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
name|aegis
operator|.
name|type
operator|.
name|DefaultTypeMapping
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
name|aegis
operator|.
name|type
operator|.
name|TypeCreationOptions
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
name|aegis
operator|.
name|type
operator|.
name|TypeCreator
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
name|aegis
operator|.
name|type
operator|.
name|TypeMapping
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
name|aegis
operator|.
name|type
operator|.
name|TypeUtil
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
name|aegis
operator|.
name|type
operator|.
name|XMLTypeCreator
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
name|aegis
operator|.
name|type
operator|.
name|basic
operator|.
name|BeanType
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
name|aegis
operator|.
name|type
operator|.
name|java5
operator|.
name|Java5TypeCreator
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
name|xmlschema
operator|.
name|XmlSchemaUtils
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
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
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
name|XmlSchemaCollection
import|;
end_import

begin_comment
comment|/**  * The Aegis Databinding context object. This object coordinates the data binding process: reading and writing  * XML. By default, this object sets up a default set of type mappings. This consists of two  * DefaultTypeMapping objects. The first is empty and has the Default, Java5, and XML TypeCreator classes  * configured. The second contains the standard mappings of the stock types. If a type can't be mapped in  * either, then the creators create a mapping and store it in the first one. The application can control some  * parameters of the type creators by creating a TypeCreationOptions object and setting properties. The  * application can add custom mappings to the type mapping, or even use its own classes for the TypeMapping or  * TypeCreator objects. Aegis, unlike JAXB, has no concept of a 'root element'. So, an application that uses  * Aegis without a web service has to either depend on xsi:type (at least for root elements) or have its own  * mapping from elements to classes, and pass the resulting Class objects to the readers. At this level, the  * application must specify the initial set of classes to make make use of untyped collections or .aegis.xml  * files. If the application leaves this list empty, and reads XML messages, then no .aegis.xml files are used  * unless the application has specified a Class&lt;T&gt; for the root of a particular item read. Specifically,  * if the application just leaves it to Aegis to map an element tagged with an xsi:type to a class, Aegis  * can't know that some arbitrary class in some arbitrary package is mapped to a particular schema type by  * QName in a mapping XML file. At the level of the CXF data binding, the 'root elements' are defined by the  * WSDL message parts. Additional classes that participate are termed 'override' classes.  */
end_comment

begin_class
specifier|public
class|class
name|AegisContext
block|{
comment|/**      * Namespace used for the miscellaneous Aegis type schema.      */
specifier|public
specifier|static
specifier|final
name|String
name|UTILITY_TYPES_SCHEMA_NS
init|=
literal|"http://cxf.apache.org/aegisTypes"
decl_stmt|;
specifier|private
name|Document
name|aegisTypesSchemaDocument
decl_stmt|;
specifier|private
name|Document
name|xmimeSchemaDocument
decl_stmt|;
specifier|private
name|boolean
name|writeXsiTypes
decl_stmt|;
specifier|private
name|boolean
name|readXsiTypes
init|=
literal|true
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|rootClassNames
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|rootClasses
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|QName
argument_list|>
name|rootTypeQNames
decl_stmt|;
comment|// this type mapping is the front of the chain of delegating type mappings.
specifier|private
name|TypeMapping
name|typeMapping
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|AegisType
argument_list|>
name|rootTypes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|beanImplementationMap
decl_stmt|;
specifier|private
name|TypeCreationOptions
name|configuration
decl_stmt|;
specifier|private
name|boolean
name|mtomEnabled
decl_stmt|;
specifier|private
name|boolean
name|mtomUseXmime
decl_stmt|;
specifier|private
name|boolean
name|enableJDOMMappings
decl_stmt|;
comment|// this URI goes into the type map.
specifier|private
name|String
name|mappingNamespaceURI
decl_stmt|;
comment|/**      * Construct a context.      */
specifier|public
name|AegisContext
parameter_list|()
block|{
name|beanImplementationMap
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|rootClasses
operator|=
operator|new
name|HashSet
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
argument_list|()
expr_stmt|;
name|rootTypeQNames
operator|=
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|TypeCreator
name|createTypeCreator
parameter_list|()
block|{
name|AbstractTypeCreator
name|xmlCreator
init|=
name|createRootTypeCreator
argument_list|()
decl_stmt|;
name|Java5TypeCreator
name|j5Creator
init|=
operator|new
name|Java5TypeCreator
argument_list|()
decl_stmt|;
name|j5Creator
operator|.
name|setNextCreator
argument_list|(
name|createDefaultTypeCreator
argument_list|()
argument_list|)
expr_stmt|;
name|j5Creator
operator|.
name|setConfiguration
argument_list|(
name|getTypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
name|xmlCreator
operator|.
name|setNextCreator
argument_list|(
name|j5Creator
argument_list|)
expr_stmt|;
return|return
name|xmlCreator
return|;
block|}
specifier|protected
name|AbstractTypeCreator
name|createRootTypeCreator
parameter_list|()
block|{
name|AbstractTypeCreator
name|creator
init|=
operator|new
name|XMLTypeCreator
argument_list|()
decl_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
name|getTypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|creator
return|;
block|}
specifier|protected
name|AbstractTypeCreator
name|createDefaultTypeCreator
parameter_list|()
block|{
name|AbstractTypeCreator
name|creator
init|=
operator|new
name|DefaultTypeCreator
argument_list|()
decl_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
name|getTypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|creator
return|;
block|}
comment|/**      * Initialize the context. The encodingStyleURI allows .aegis.xml files to have multiple mappings for,      * say, SOAP 1.1 versus SOAP 1.2. Passing null uses a default URI.      *       * @param mappingNamespaceURI URI to select mappings based on the encoding.      */
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|// allow spring config of an alternative mapping.
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|TypeCreationOptions
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|typeMapping
operator|==
literal|null
condition|)
block|{
name|boolean
name|defaultNillable
init|=
name|configuration
operator|.
name|isDefaultNillable
argument_list|()
decl_stmt|;
name|TypeMapping
name|baseTM
init|=
name|DefaultTypeMapping
operator|.
name|createDefaultTypeMapping
argument_list|(
name|defaultNillable
argument_list|,
name|mtomUseXmime
argument_list|,
name|enableJDOMMappings
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappingNamespaceURI
operator|==
literal|null
condition|)
block|{
name|mappingNamespaceURI
operator|=
name|DefaultTypeMapping
operator|.
name|DEFAULT_MAPPING_URI
expr_stmt|;
block|}
name|DefaultTypeMapping
name|defaultTypeMapping
init|=
operator|new
name|DefaultTypeMapping
argument_list|(
name|mappingNamespaceURI
argument_list|,
name|baseTM
argument_list|)
decl_stmt|;
name|defaultTypeMapping
operator|.
name|setTypeCreator
argument_list|(
name|createTypeCreator
argument_list|()
argument_list|)
expr_stmt|;
name|typeMapping
operator|=
name|defaultTypeMapping
expr_stmt|;
block|}
name|processRootTypes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|AegisReader
argument_list|<
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
argument_list|>
name|createDomElementReader
parameter_list|()
block|{
return|return
operator|new
name|AegisElementDataReader
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|AegisReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|createXMLStreamReader
parameter_list|()
block|{
return|return
operator|new
name|AegisXMLStreamDataReader
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|AegisWriter
argument_list|<
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
argument_list|>
name|createDomElementWriter
parameter_list|()
block|{
return|return
operator|new
name|AegisElementDataWriter
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|AegisWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|createXMLStreamWriter
parameter_list|()
block|{
return|return
operator|new
name|AegisXMLStreamDataWriter
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * If a class was provided as part of the 'root' list, retrieve it's AegisType by Class.      *       * @param clazz      * @return      */
specifier|public
name|AegisType
name|getRootType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|rootClasses
operator|.
name|contains
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|typeMapping
operator|.
name|getType
argument_list|(
name|clazz
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * If a class was provided as part of the root list, retrieve it's AegisType by schema type QName.      *       * @param schemaTypeName      * @return      */
specifier|public
name|AegisType
name|getRootType
parameter_list|(
name|QName
name|schemaTypeName
parameter_list|)
block|{
if|if
condition|(
name|rootTypeQNames
operator|.
name|contains
argument_list|(
name|schemaTypeName
argument_list|)
condition|)
block|{
return|return
name|typeMapping
operator|.
name|getType
argument_list|(
name|schemaTypeName
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|rootMappableClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|mappableClasses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|jtype
range|:
name|rootClasses
control|)
block|{
name|addTypeToMappableClasses
argument_list|(
name|mappableClasses
argument_list|,
name|jtype
argument_list|)
expr_stmt|;
block|}
return|return
name|mappableClasses
return|;
block|}
specifier|private
name|void
name|addTypeToMappableClasses
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|mappableClasses
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|jtype
parameter_list|)
block|{
if|if
condition|(
name|jtype
operator|instanceof
name|Class
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|jclass
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|jtype
decl_stmt|;
if|if
condition|(
name|jclass
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|mappableClasses
operator|.
name|add
argument_list|(
name|jclass
operator|.
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mappableClasses
operator|.
name|add
argument_list|(
name|jclass
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jtype
operator|instanceof
name|ParameterizedType
condition|)
block|{
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|t2
range|:
operator|(
operator|(
name|ParameterizedType
operator|)
name|jtype
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
control|)
block|{
name|addTypeToMappableClasses
argument_list|(
name|mappableClasses
argument_list|,
name|t2
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|jtype
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|GenericArrayType
name|gt
init|=
operator|(
name|GenericArrayType
operator|)
name|jtype
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|ct
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|gt
operator|.
name|getGenericComponentType
argument_list|()
decl_stmt|;
comment|// this looks nutty, but there's no other way. Make an array and take it's class.
name|ct
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|ct
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Examine a list of override classes, and register all of them.      *       * @param tm type manager for this binding      * @param classes list of class names      */
specifier|private
name|void
name|processRootTypes
parameter_list|()
block|{
name|rootTypes
operator|=
operator|new
name|HashSet
argument_list|<
name|AegisType
argument_list|>
argument_list|()
expr_stmt|;
comment|// app may have already supplied classes.
if|if
condition|(
name|rootClasses
operator|==
literal|null
condition|)
block|{
name|rootClasses
operator|=
operator|new
name|HashSet
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|rootTypeQNames
operator|=
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|rootClassNames
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|typeName
range|:
name|rootClassNames
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
literal|null
decl_stmt|;
try|try
block|{
name|c
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|typeName
argument_list|,
name|TypeUtil
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not find override type class: "
operator|+
name|typeName
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|rootClasses
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
comment|// This is a list of AegisType rather than Class so that it can set up for generic collections.
comment|// When we see a generic, we process both the generic outer class and each parameter class.
comment|// This is not the same thing as allowing mappings of arbitrary x<q> types.
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|rootMappableClassSet
init|=
name|rootMappableClasses
argument_list|()
decl_stmt|;
comment|/*          * First loop: process non-Class roots, creating full types for them          * and registering them.          */
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|reflectType
range|:
name|rootClasses
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|reflectType
operator|instanceof
name|Class
operator|)
condition|)
block|{
comment|// if it's not a Class, it can't be mapped from Class to type in the mapping.
comment|// so we create
name|AegisType
name|aegisType
init|=
name|typeMapping
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|reflectType
argument_list|)
decl_stmt|;
name|typeMapping
operator|.
name|register
argument_list|(
name|aegisType
argument_list|)
expr_stmt|;
comment|// note: we don't handle arbitrary generics, so no BeanType
comment|// check here.
name|rootTypeQNames
operator|.
name|add
argument_list|(
name|aegisType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*          * Second loop: process Class roots, including those derived from           * generic types, creating when not in the default mappings.          */
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|rootMappableClassSet
control|)
block|{
name|AegisType
name|t
init|=
name|typeMapping
operator|.
name|getType
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|typeMapping
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|register
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
name|rootTypeQNames
operator|.
name|add
argument_list|(
name|t
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|BeanType
condition|)
block|{
name|BeanType
name|bt
init|=
operator|(
name|BeanType
operator|)
name|t
decl_stmt|;
name|bt
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|setExtension
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rootTypes
operator|.
name|add
argument_list|(
name|bt
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|schemaImportsUtilityTypes
parameter_list|(
name|XmlSchema
name|schema
parameter_list|)
block|{
return|return
name|XmlSchemaUtils
operator|.
name|schemaImportsNamespace
argument_list|(
name|schema
argument_list|,
name|UTILITY_TYPES_SCHEMA_NS
argument_list|)
return|;
block|}
specifier|private
name|Document
name|getSchemaDocument
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
try|try
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resourcePath
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
comment|// could we make these documents static? What would we synchronize on?
specifier|private
name|Document
name|getAegisTypesSchemaDocument
parameter_list|()
block|{
if|if
condition|(
name|aegisTypesSchemaDocument
operator|==
literal|null
condition|)
block|{
name|aegisTypesSchemaDocument
operator|=
name|getSchemaDocument
argument_list|(
literal|"/META-INF/cxf/aegisTypes.xsd"
argument_list|)
expr_stmt|;
block|}
return|return
name|aegisTypesSchemaDocument
return|;
block|}
specifier|private
name|Document
name|getXmimeSchemaDocument
parameter_list|()
block|{
if|if
condition|(
name|xmimeSchemaDocument
operator|==
literal|null
condition|)
block|{
name|xmimeSchemaDocument
operator|=
name|getSchemaDocument
argument_list|(
literal|"/schemas/wsdl/xmime.xsd"
argument_list|)
expr_stmt|;
block|}
return|return
name|xmimeSchemaDocument
return|;
block|}
specifier|public
name|XmlSchema
name|addTypesSchemaDocument
parameter_list|(
name|XmlSchemaCollection
name|collection
parameter_list|)
block|{
return|return
name|collection
operator|.
name|read
argument_list|(
name|getAegisTypesSchemaDocument
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
name|addXmimeSchemaDocument
parameter_list|(
name|XmlSchemaCollection
name|collection
parameter_list|)
block|{
return|return
name|collection
operator|.
name|read
argument_list|(
name|getXmimeSchemaDocument
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|addUtilityTypesToSchema
parameter_list|(
name|XmlSchema
name|root
parameter_list|)
block|{
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|root
argument_list|,
name|UTILITY_TYPES_SCHEMA_NS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve the set of root class names. Note that if the application specifies the root classes by Class      * instead of by name, this will return null.      *       * @return      */
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getRootClassNames
parameter_list|()
block|{
return|return
name|rootClassNames
return|;
block|}
comment|/**      * Set the root class names. This function is a convenience for Spring configuration. It sets the same      * underlying collection as {@link #setRootClasses(Set)}.      *       * @param classNames      */
specifier|public
name|void
name|setRootClassNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|classNames
parameter_list|)
block|{
name|rootClassNames
operator|=
name|classNames
expr_stmt|;
block|}
comment|/**      * Return the type mapping configuration associated with this context.      *       * @return Returns the configuration.      */
specifier|public
name|TypeCreationOptions
name|getTypeCreationOptions
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Set the configuration object. The configuration specifies default type mapping behaviors.      *       * @param configuration The configuration to set.      */
specifier|public
name|void
name|setTypeCreationOptions
parameter_list|(
name|TypeCreationOptions
name|newConfiguration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|newConfiguration
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWriteXsiTypes
parameter_list|()
block|{
return|return
name|writeXsiTypes
return|;
block|}
specifier|public
name|boolean
name|isReadXsiTypes
parameter_list|()
block|{
return|return
name|readXsiTypes
return|;
block|}
comment|/**      * Controls whether Aegis writes xsi:type attributes on all elements. False by default.      *       * @param flag      */
specifier|public
name|void
name|setWriteXsiTypes
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|writeXsiTypes
operator|=
name|flag
expr_stmt|;
block|}
comment|/**      * Controls the use of xsi:type attributes when reading objects. By default, xsi:type reading is enabled.      * When disabled, Aegis will only map for objects that the application manually maps in the type mapping.      *       * @param flag      */
specifier|public
name|void
name|setReadXsiTypes
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|readXsiTypes
operator|=
name|flag
expr_stmt|;
block|}
comment|/**      * Return the type mapping object used by this context.      *       * @return      */
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|typeMapping
return|;
block|}
comment|/**      * Set the type mapping object used by this context.      *       * @param typeMapping      */
specifier|public
name|void
name|setTypeMapping
parameter_list|(
name|TypeMapping
name|typeMapping
parameter_list|)
block|{
name|this
operator|.
name|typeMapping
operator|=
name|typeMapping
expr_stmt|;
block|}
comment|/**      * Retrieve the Aegis type objects for the root classes.      *       * @return the set of type objects.      */
specifier|public
name|Set
argument_list|<
name|AegisType
argument_list|>
name|getRootTypes
parameter_list|()
block|{
return|return
name|rootTypes
return|;
block|}
comment|/**      * This property provides support for interfaces. If there is a mapping from an interface's Class<T> to a      * string containing a class name, Aegis will create proxy objects of that class name.      *       * @see org.apache.cxf.aegis.type.basic.BeanType      * @return      */
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|getBeanImplementationMap
parameter_list|()
block|{
return|return
name|beanImplementationMap
return|;
block|}
specifier|public
name|void
name|setBeanImplementationMap
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|beanImplementationMap
parameter_list|)
block|{
name|this
operator|.
name|beanImplementationMap
operator|=
name|beanImplementationMap
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|getRootClasses
parameter_list|()
block|{
return|return
name|rootClasses
return|;
block|}
comment|/**      * The list of initial classes.      *       * @param rootClasses      */
specifier|public
name|void
name|setRootClasses
parameter_list|(
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|rootClasses
parameter_list|)
block|{
name|this
operator|.
name|rootClasses
operator|=
name|rootClasses
expr_stmt|;
block|}
comment|/**      * Is MTOM enabled in this context?      *       * @return      */
specifier|public
name|boolean
name|isMtomEnabled
parameter_list|()
block|{
return|return
name|mtomEnabled
return|;
block|}
specifier|public
name|void
name|setMtomEnabled
parameter_list|(
name|boolean
name|mtomEnabled
parameter_list|)
block|{
name|this
operator|.
name|mtomEnabled
operator|=
name|mtomEnabled
expr_stmt|;
block|}
comment|/**      * Should this service use schema for MTOM types xmime:base64Binary instead of xsd:base64Binary?      *       * @return      */
specifier|public
name|boolean
name|isMtomUseXmime
parameter_list|()
block|{
return|return
name|mtomUseXmime
return|;
block|}
specifier|public
name|void
name|setMtomUseXmime
parameter_list|(
name|boolean
name|mtomUseXmime
parameter_list|)
block|{
name|this
operator|.
name|mtomUseXmime
operator|=
name|mtomUseXmime
expr_stmt|;
block|}
comment|/**      * What URI identifies the type mapping for this context? When the XMLTypeCreator reads .aegis.xml file,      * it will only read mappings for this URI (or no URI). When the abstract type creator is otherwise at a      * loss for a namespace URI, it will use this URI.      *       * @return      */
specifier|public
name|String
name|getMappingNamespaceURI
parameter_list|()
block|{
return|return
name|mappingNamespaceURI
return|;
block|}
specifier|public
name|void
name|setMappingNamespaceURI
parameter_list|(
name|String
name|mappingNamespaceURI
parameter_list|)
block|{
name|this
operator|.
name|mappingNamespaceURI
operator|=
name|mappingNamespaceURI
expr_stmt|;
if|if
condition|(
name|typeMapping
operator|!=
literal|null
condition|)
block|{
name|typeMapping
operator|.
name|setMappingIdentifierURI
argument_list|(
name|mappingNamespaceURI
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isEnableJDOMMappings
parameter_list|()
block|{
return|return
name|enableJDOMMappings
return|;
block|}
comment|/**      * Whether to enable JDOM as a mapping for xsd:anyType if JDOM is in the classpath.       * @param enableJDOMMappings      */
specifier|public
name|void
name|setEnableJDOMMappings
parameter_list|(
name|boolean
name|enableJDOMMappings
parameter_list|)
block|{
name|this
operator|.
name|enableJDOMMappings
operator|=
name|enableJDOMMappings
expr_stmt|;
block|}
block|}
end_class

end_unit

