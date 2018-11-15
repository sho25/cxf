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
name|basic
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|DatabindingException
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
name|util
operator|.
name|NamespaceHelper
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
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_class
specifier|public
class|class
name|XMLBeanTypeInfo
extends|extends
name|BeanTypeInfo
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|XMLBeanTypeInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Element
argument_list|>
name|mappings
decl_stmt|;
comment|/**      * Map used for storing meta data about each property      */
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|BeanTypePropertyInfo
argument_list|>
name|name2PropertyInfo
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|XMLBeanTypeInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|mappings
parameter_list|,
name|String
name|defaultNS
parameter_list|)
block|{
name|super
argument_list|(
name|typeClass
argument_list|,
name|defaultNS
argument_list|)
expr_stmt|;
name|this
operator|.
name|mappings
operator|=
name|mappings
expr_stmt|;
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
name|Element
name|e
init|=
name|getPropertyElement
argument_list|(
name|desc
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"type"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|super
operator|.
name|registerType
argument_list|(
name|desc
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapProperty
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
name|Element
name|e
init|=
name|getPropertyElement
argument_list|(
name|pd
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|style
init|=
literal|null
decl_stmt|;
name|QName
name|mappedName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|String
name|ignore
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"ignore"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|ignore
argument_list|)
condition|)
block|{
return|return;
block|}
name|LOG
operator|.
name|finest
argument_list|(
literal|"Found mapping for property "
operator|+
name|pd
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|style
operator|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"style"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|style
operator|==
literal|null
condition|)
block|{
name|style
operator|=
literal|"element"
expr_stmt|;
block|}
name|boolean
name|element
init|=
literal|"element"
operator|.
name|equals
argument_list|(
name|style
argument_list|)
decl_stmt|;
name|boolean
name|qualify
decl_stmt|;
if|if
condition|(
name|element
condition|)
block|{
name|qualify
operator|=
name|isQualifyElements
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|qualify
operator|=
name|isQualifyAttributes
argument_list|()
expr_stmt|;
block|}
name|String
name|namespace
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|qualify
condition|)
block|{
name|namespace
operator|=
name|getDefaultNamespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|mappedName
operator|=
name|NamespaceHelper
operator|.
name|createQName
argument_list|(
name|e
argument_list|,
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"mappedName"
argument_list|)
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mappedName
operator|==
literal|null
condition|)
block|{
name|mappedName
operator|=
name|createMappedName
argument_list|(
name|pd
argument_list|,
name|qualify
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|QName
name|mappedType
init|=
name|NamespaceHelper
operator|.
name|createQName
argument_list|(
name|e
argument_list|,
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"typeName"
argument_list|)
argument_list|,
name|getDefaultNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedType
operator|!=
literal|null
condition|)
block|{
name|mapTypeName
argument_list|(
name|mappedName
argument_list|,
name|mappedType
argument_list|)
expr_stmt|;
block|}
comment|/*              * Whenever we create a type object, it has to have a schema type. If we created a custom type              * object out of thin air here, we've may have a problem. If "typeName" was specified, then then              * we know the mapping. But if mappedName was not specified, then the typeName will come from the              * type mapping, so we have to ask it. And if some other type creator has something to say about              * it, we'll get it wrong.              */
name|String
name|explicitTypeName
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|explicitTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|explicitTypeName
argument_list|,
name|XMLBeanTypeInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|AegisType
name|customTypeObject
init|=
operator|(
name|AegisType
operator|)
name|typeClass
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|mapType
argument_list|(
name|mappedName
argument_list|,
name|customTypeObject
argument_list|)
expr_stmt|;
name|QName
name|schemaType
init|=
name|mappedType
decl_stmt|;
if|if
condition|(
name|schemaType
operator|==
literal|null
condition|)
block|{
name|schemaType
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getTypeQName
argument_list|(
name|pd
operator|.
name|getPropertyType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|customTypeObject
operator|.
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
name|customTypeObject
operator|.
name|setSchemaType
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|InstantiationException
decl||
name|IllegalAccessException
name|e1
parameter_list|)
block|{
comment|//
block|}
block|}
name|String
name|nillableVal
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"nillable"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nillableVal
operator|!=
literal|null
operator|&&
name|nillableVal
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ensurePropertyInfo
argument_list|(
name|mappedName
argument_list|)
operator|.
name|setNillable
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|nillableVal
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|minOccurs
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"minOccurs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|minOccurs
operator|!=
literal|null
operator|&&
name|minOccurs
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ensurePropertyInfo
argument_list|(
name|mappedName
argument_list|)
operator|.
name|setMinOccurs
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|minOccurs
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|maxOccurs
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"maxOccurs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxOccurs
operator|!=
literal|null
operator|&&
name|maxOccurs
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ensurePropertyInfo
argument_list|(
name|mappedName
argument_list|)
operator|.
name|setMinOccurs
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|maxOccurs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
comment|// logger.debug("Mapped " + pd.getName() + " as " + style + " with
comment|// name " + mappedName);
if|if
condition|(
literal|"element"
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|mapElement
argument_list|(
name|pd
operator|.
name|getName
argument_list|()
argument_list|,
name|mappedName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"attribute"
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|mapAttribute
argument_list|(
name|pd
operator|.
name|getName
argument_list|()
argument_list|,
name|mappedName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid style: "
operator|+
name|style
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|DatabindingException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|prepend
argument_list|(
literal|"Couldn't create type for property "
operator|+
name|pd
operator|.
name|getName
argument_list|()
operator|+
literal|" on "
operator|+
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
name|Element
name|getPropertyElement
parameter_list|(
name|String
name|name2
parameter_list|)
block|{
for|for
control|(
name|Element
name|mapping2
range|:
name|mappings
control|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elements
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|mapping2
argument_list|,
literal|""
argument_list|,
literal|"property"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|e
init|=
name|elements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|DOMUtils
operator|.
name|getAttributeValueEmptyNull
argument_list|(
name|e
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|name2
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Grab Nillable by looking in PropertyInfo map if no entry found, revert to      * parent class      */
annotation|@
name|Override
specifier|public
name|boolean
name|isNillable
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|BeanTypePropertyInfo
name|info
init|=
name|getPropertyInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
operator|.
name|isNillable
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|isNillable
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Return minOccurs if specified in the XML, otherwise from the defaults      * in the base class.      */
annotation|@
name|Override
specifier|public
name|int
name|getMinOccurs
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|BeanTypePropertyInfo
name|info
init|=
name|getPropertyInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
operator|.
name|getMinOccurs
argument_list|()
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
comment|/**      * Return maxOccurs if specified in the XML, otherwise from the      * default in the base class.      */
annotation|@
name|Override
specifier|public
name|int
name|getMaxOccurs
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|BeanTypePropertyInfo
name|info
init|=
name|getPropertyInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
operator|.
name|getMaxOccurs
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getMaxOccurs
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Grab the Property Info for the given property      *      * @param name      * @return the BeanTypePropertyInfo for the property or NULL if none found      */
specifier|private
name|BeanTypePropertyInfo
name|getPropertyInfo
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|name2PropertyInfo
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Grab the Property Info for the given property but if not found create one      * and add it to the map      *      * @param name      * @return the BeanTypePropertyInfo for the property      */
specifier|private
name|BeanTypePropertyInfo
name|ensurePropertyInfo
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|BeanTypePropertyInfo
name|result
init|=
name|getPropertyInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|BeanTypePropertyInfo
argument_list|()
expr_stmt|;
name|name2PropertyInfo
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

