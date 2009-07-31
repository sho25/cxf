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
operator|.
name|tools
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|common
operator|.
name|xmlschema
operator|.
name|XmlSchemaConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|DefaultValueWriter
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
name|tools
operator|.
name|util
operator|.
name|ClassCollector
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
name|tools
operator|.
name|wsdlto
operator|.
name|core
operator|.
name|DataBindingProfile
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
name|generate
operator|.
name|XSD2JavaGenerator
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
name|helper
operator|.
name|HelperContextImpl
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
name|helper
operator|.
name|XSDHelperImpl
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
name|eclipse
operator|.
name|emf
operator|.
name|codegen
operator|.
name|ecore
operator|.
name|genmodel
operator|.
name|GenClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|codegen
operator|.
name|ecore
operator|.
name|genmodel
operator|.
name|GenModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|codegen
operator|.
name|ecore
operator|.
name|genmodel
operator|.
name|GenPackage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|EClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|EClassifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|EPackage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|impl
operator|.
name|EPackageRegistryImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|util
operator|.
name|BasicExtendedMetaData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|emf
operator|.
name|ecore
operator|.
name|util
operator|.
name|ExtendedMetaData
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
name|Property
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
name|helper
operator|.
name|TypeHelper
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
name|XSDHelper
import|;
end_import

begin_class
specifier|public
class|class
name|SDODatabinding
extends|extends
name|XSD2JavaGenerator
implements|implements
name|DataBindingProfile
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DATABINDING_DYNAMIC_SDO
init|=
literal|"sdo-dynamic"
decl_stmt|;
specifier|private
name|TypeHelper
name|typeHelper
decl_stmt|;
specifier|private
name|XSDHelper
name|xsdHelper
decl_stmt|;
specifier|private
name|boolean
name|dynamic
decl_stmt|;
specifier|private
name|ExtendedMetaData
name|extendedMetaData
decl_stmt|;
specifier|private
name|GenModel
name|genModel
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|EClassifier
argument_list|,
name|GenClass
argument_list|>
name|genClasses
init|=
operator|new
name|HashMap
argument_list|<
name|EClassifier
argument_list|,
name|GenClass
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|SchemaCollection
name|schemaCollection
decl_stmt|;
specifier|private
name|EPackage
operator|.
name|Registry
name|packageRegistry
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|ToolContext
name|context
parameter_list|)
throws|throws
name|ToolException
block|{
name|String
name|databinding
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_DATABINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|DATABINDING_DYNAMIC_SDO
operator|.
name|equalsIgnoreCase
argument_list|(
name|databinding
argument_list|)
condition|)
block|{
name|dynamic
operator|=
literal|true
expr_stmt|;
block|}
name|generatedPackages
operator|=
literal|null
expr_stmt|;
name|String
name|outputDir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
decl_stmt|;
name|String
name|pkg
init|=
name|context
operator|.
name|getPackageName
argument_list|()
decl_stmt|;
comment|// preparing the directories where files to be written.
name|File
name|targetDir
decl_stmt|;
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
name|String
name|wsdl
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLOCATION
argument_list|)
decl_stmt|;
try|try
block|{
name|outputDir
operator|=
operator|new
name|File
argument_list|(
operator|new
name|URI
argument_list|(
name|wsdl
argument_list|)
argument_list|)
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|outputDir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
block|}
name|targetDir
operator|=
operator|new
name|File
argument_list|(
name|outputDir
argument_list|)
expr_stmt|;
name|targetDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|argList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|argList
operator|.
name|add
argument_list|(
literal|"-targetDirectory"
argument_list|)
expr_stmt|;
name|argList
operator|.
name|add
argument_list|(
name|targetDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|pkg
operator|!=
literal|null
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
literal|"-javaPackage"
argument_list|)
expr_stmt|;
name|argList
operator|.
name|add
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
name|schemaCollection
operator|=
operator|(
name|SchemaCollection
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|XML_SCHEMA_COLLECTION
argument_list|)
expr_stmt|;
name|argList
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|//bogus arg
name|String
index|[]
name|args
init|=
name|argList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|argList
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|packageRegistry
operator|=
operator|new
name|EPackageRegistryImpl
argument_list|(
name|EPackage
operator|.
name|Registry
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|extendedMetaData
operator|=
operator|new
name|BasicExtendedMetaData
argument_list|(
name|packageRegistry
argument_list|)
expr_stmt|;
name|HelperContext
name|hc
init|=
operator|new
name|HelperContextImpl
argument_list|(
name|extendedMetaData
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|xsdHelper
operator|=
name|hc
operator|.
name|getXSDHelper
argument_list|()
expr_stmt|;
name|typeHelper
operator|=
name|hc
operator|.
name|getTypeHelper
argument_list|()
expr_stmt|;
name|processArguments
argument_list|(
name|args
argument_list|)
expr_stmt|;
operator|(
operator|(
name|XSDHelperImpl
operator|)
name|xsdHelper
operator|)
operator|.
name|setRedefineBuiltIn
argument_list|(
name|generateBuiltIn
argument_list|)
expr_stmt|;
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|schema
operator|.
name|write
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|xsdHelper
operator|.
name|define
argument_list|(
operator|new
name|StringReader
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|schema
operator|.
name|getSourceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|context
parameter_list|)
throws|throws
name|ToolException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns2pkgMap
init|=
name|context
operator|.
name|getNamespacePackageMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PackageInfo
argument_list|>
name|packageInfoTable
init|=
name|createPackageInfoTable
argument_list|(
name|schemaCollection
argument_list|,
name|ns2pkgMap
argument_list|)
decl_stmt|;
name|ClassCollector
name|classCollector
init|=
name|context
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|dynamic
condition|)
block|{
comment|// No XSD2Java is needed for dynamic SDO
name|genModel
operator|=
name|generatePackages
argument_list|(
name|packageRegistry
operator|.
name|values
argument_list|()
argument_list|,
name|targetDirectory
argument_list|,
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|PackageInfo
argument_list|>
argument_list|(
name|packageInfoTable
argument_list|)
argument_list|,
name|genOptions
argument_list|,
name|allNamespaces
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|GenPackage
argument_list|>
name|packages
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|genModel
operator|.
name|getGenPackages
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|GenPackage
argument_list|>
name|iter
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
comment|// loop through the list, once to build up the eclass to genclass mapper
name|GenPackage
name|genPackage
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|GenClass
argument_list|>
name|classes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|genPackage
operator|.
name|getGenClasses
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|GenClass
argument_list|>
name|classIter
init|=
name|classes
operator|.
name|iterator
argument_list|()
init|;
name|classIter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|GenClass
name|genClass
init|=
name|classIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|genClasses
operator|.
name|put
argument_list|(
name|genClass
operator|.
name|getEcoreClass
argument_list|()
argument_list|,
name|genClass
argument_list|)
expr_stmt|;
comment|//This gets the "impl" classes, how do we get everything else?
name|String
name|s
init|=
name|genClass
operator|.
name|getQualifiedClassName
argument_list|()
decl_stmt|;
name|String
name|p
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|classCollector
operator|.
name|addTypesClassName
argument_list|(
name|p
argument_list|,
name|s
argument_list|,
name|genClass
operator|.
name|getQualifiedClassName
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|=
name|genClass
operator|.
name|getGenPackage
argument_list|()
operator|.
name|getInterfacePackageName
argument_list|()
expr_stmt|;
name|s
operator|=
name|genClass
operator|.
name|getInterfaceName
argument_list|()
expr_stmt|;
name|classCollector
operator|.
name|addTypesClassName
argument_list|(
name|p
argument_list|,
name|s
argument_list|,
name|p
operator|+
literal|"."
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
name|String
name|p
init|=
name|genPackage
operator|.
name|getInterfacePackageName
argument_list|()
decl_stmt|;
name|String
name|s
init|=
name|genPackage
operator|.
name|getFactoryInterfaceName
argument_list|()
decl_stmt|;
name|classCollector
operator|.
name|addTypesClassName
argument_list|(
name|p
argument_list|,
name|s
argument_list|,
name|p
operator|+
literal|"."
operator|+
name|s
argument_list|)
expr_stmt|;
name|p
operator|=
name|genPackage
operator|.
name|getClassPackageName
argument_list|()
expr_stmt|;
name|s
operator|=
name|genPackage
operator|.
name|getFactoryClassName
argument_list|()
expr_stmt|;
name|classCollector
operator|.
name|addTypesClassName
argument_list|(
name|p
argument_list|,
name|s
argument_list|,
name|p
operator|+
literal|"."
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PackageInfo
argument_list|>
name|createPackageInfoTable
parameter_list|(
name|SchemaCollection
name|schemas
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns2PkgMap
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PackageInfo
argument_list|>
name|packageInfoTable
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PackageInfo
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns2PkgMap
operator|!=
literal|null
operator|&&
operator|!
name|ns2PkgMap
operator|.
name|isEmpty
argument_list|()
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
name|String
argument_list|>
name|e
range|:
name|ns2PkgMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|packageInfoTable
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
operator|new
name|PackageInfo
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
literal|null
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemas
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
name|packageInfoTable
operator|.
name|put
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
operator|new
name|PackageInfo
argument_list|(
name|javaPackage
argument_list|,
name|prefix
argument_list|,
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|packageInfoTable
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|(
name|QName
name|qName
parameter_list|,
name|boolean
name|element
parameter_list|)
block|{
name|Type
name|type
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|element
condition|)
block|{
name|Property
name|property
init|=
name|xsdHelper
operator|.
name|getGlobalProperty
argument_list|(
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|property
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|typeHelper
operator|.
name|getType
argument_list|(
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|getClassName
argument_list|(
name|type
argument_list|)
return|;
block|}
return|return
name|DataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|String
name|getClassName
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|EClassifier
name|eClassifier
init|=
operator|(
name|EClassifier
operator|)
name|type
decl_stmt|;
name|String
name|name
init|=
name|eClassifier
operator|.
name|getInstanceClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
if|if
condition|(
name|genModel
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|dynamic
condition|)
block|{
return|return
name|DataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|type
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|eClassifier
operator|instanceof
name|EClass
condition|)
block|{
comment|// complex type
name|GenClass
name|genEClass
init|=
operator|(
name|GenClass
operator|)
name|genClasses
operator|.
name|get
argument_list|(
name|eClassifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|genEClass
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|genEClass
operator|.
name|getGenPackage
argument_list|()
operator|.
name|getInterfacePackageName
argument_list|()
operator|+
literal|'.'
operator|+
name|genEClass
operator|.
name|getInterfaceName
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// simple type
name|name
operator|=
name|eClassifier
operator|.
name|getInstanceClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getWrappedElementType
parameter_list|(
name|QName
name|wrapperElement
parameter_list|,
name|QName
name|item
parameter_list|)
block|{
name|Type
name|type
init|=
literal|null
decl_stmt|;
name|Property
name|property
init|=
name|xsdHelper
operator|.
name|getGlobalProperty
argument_list|(
name|wrapperElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|wrapperElement
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|property
operator|.
name|getType
argument_list|()
expr_stmt|;
name|Property
name|itemProp
init|=
name|type
operator|.
name|getProperty
argument_list|(
name|item
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|itemProp
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|itemProp
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|getClassName
argument_list|(
name|type
argument_list|)
return|;
block|}
return|return
name|DataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|DefaultValueWriter
name|createDefaultValueWriter
parameter_list|(
name|QName
name|qName
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
comment|// since we dont need any sample client/server code with default values we return null
return|return
literal|null
return|;
block|}
specifier|public
name|DefaultValueWriter
name|createDefaultValueWriterForWrappedElement
parameter_list|(
name|QName
name|qName
parameter_list|,
name|QName
name|qName1
parameter_list|)
block|{
comment|// since we dont need any sample client/server code with default values we return null
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

