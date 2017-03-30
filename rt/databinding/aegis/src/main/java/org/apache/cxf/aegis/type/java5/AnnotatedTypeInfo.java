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
operator|.
name|type
operator|.
name|java5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
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
name|basic
operator|.
name|BeanTypeInfo
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
name|util
operator|.
name|NamespaceHelper
import|;
end_import

begin_class
specifier|public
class|class
name|AnnotatedTypeInfo
extends|extends
name|BeanTypeInfo
block|{
specifier|private
specifier|final
name|AnnotationReader
name|annotationReader
decl_stmt|;
specifier|public
name|AnnotatedTypeInfo
parameter_list|(
name|TypeMapping
name|tm
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|String
name|ns
parameter_list|,
name|TypeCreationOptions
name|typeCreationOptions
parameter_list|)
block|{
name|this
argument_list|(
name|tm
argument_list|,
name|typeClass
argument_list|,
name|ns
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
argument_list|,
name|typeCreationOptions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AnnotatedTypeInfo
parameter_list|(
name|TypeMapping
name|tm
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|String
name|ns
parameter_list|,
name|AnnotationReader
name|annotationReader
parameter_list|,
name|TypeCreationOptions
name|typeCreationOptions
parameter_list|)
block|{
name|super
argument_list|(
name|typeClass
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|this
operator|.
name|annotationReader
operator|=
name|annotationReader
expr_stmt|;
name|setQualifyAttributes
argument_list|(
name|typeCreationOptions
operator|.
name|isQualifyAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|setQualifyElements
argument_list|(
name|typeCreationOptions
operator|.
name|isQualifyElements
argument_list|()
argument_list|)
expr_stmt|;
name|setTypeMapping
argument_list|(
name|tm
argument_list|)
expr_stmt|;
name|initialize
argument_list|()
expr_stmt|;
block|}
comment|/**      * Override from parent in order to check for IgnoreProperty annotation.      */
specifier|protected
name|void
name|mapProperty
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
comment|// skip ignored properties
if|if
condition|(
name|annotationReader
operator|.
name|isIgnored
argument_list|(
name|pd
operator|.
name|getReadMethod
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|explicitNamespace
init|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|pd
operator|.
name|getReadMethod
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|mustQualify
init|=
literal|null
operator|!=
name|explicitNamespace
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|explicitNamespace
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|pd
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|isAttribute
argument_list|(
name|pd
argument_list|)
condition|)
block|{
name|mapAttribute
argument_list|(
name|name
argument_list|,
name|createMappedName
argument_list|(
name|pd
argument_list|,
name|mustQualify
operator|||
name|isQualifyAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isElement
argument_list|(
name|pd
argument_list|)
condition|)
block|{
name|mapElement
argument_list|(
name|name
argument_list|,
name|createMappedName
argument_list|(
name|pd
argument_list|,
name|mustQualify
operator|||
name|isQualifyElements
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|registerType
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
name|Method
name|readMethod
init|=
name|desc
operator|.
name|getReadMethod
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|annotationReader
operator|.
name|getType
argument_list|(
name|readMethod
argument_list|)
decl_stmt|;
return|return
name|type
operator|==
literal|null
operator|&&
name|super
operator|.
name|registerType
argument_list|(
name|desc
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isAttribute
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
return|return
name|annotationReader
operator|.
name|isAttribute
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isElement
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
return|return
operator|!
name|isAttribute
argument_list|(
name|desc
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|QName
name|createMappedName
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|,
name|boolean
name|qualify
parameter_list|)
block|{
name|QName
name|name
init|=
name|createQName
argument_list|(
name|desc
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|qualify
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
literal|null
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|name
return|;
block|}
block|}
specifier|protected
name|QName
name|createQName
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
name|String
name|name
init|=
name|getName
argument_list|(
name|desc
argument_list|)
decl_stmt|;
name|String
name|namespace
init|=
name|getNamespace
argument_list|(
name|desc
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|/**      * XML Name of a field is derived from the following sources in this order of priorities:      *<ul>      *<li> getter method annotation      *<li> field annotation      *<li> field name      *</ul>      */
specifier|private
name|String
name|getName
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
name|String
name|name
init|=
name|annotationReader
operator|.
name|getName
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|annotationReader
operator|.
name|getName
argument_list|(
name|getField
argument_list|(
name|desc
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|desc
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * XML Namespace of a field is derived from the following sources in this order of priorities:      *<ol>      *<li> getter method annotation      *<li> field annotation      *<li> class annotation      *<li> package annotation      *<li> fully qualified package name      *</ol>      */
specifier|private
name|String
name|getNamespace
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
name|String
name|namespace
init|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|getField
argument_list|(
name|desc
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|getTypeClass
argument_list|()
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
name|NamespaceHelper
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|getTypeClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
block|}
return|return
name|namespace
return|;
block|}
specifier|private
name|Field
name|getField
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
try|try
block|{
return|return
name|getTypeClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|desc
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|boolean
name|isNillable
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|PropertyDescriptor
name|desc
init|=
name|getPropertyDescriptorFromMappedName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotationReader
operator|.
name|isElement
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|annotationReader
operator|.
name|isNillable
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|isNillable
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
specifier|public
name|int
name|getMinOccurs
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|PropertyDescriptor
name|desc
init|=
name|getPropertyDescriptorFromMappedName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotationReader
operator|.
name|isElement
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|annotationReader
operator|.
name|getMinOccurs
argument_list|(
name|desc
operator|.
name|getReadMethod
argument_list|()
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getMinOccurs
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

