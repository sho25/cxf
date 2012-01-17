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
name|BeanInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|Introspector
import|;
end_import

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|common
operator|.
name|util
operator|.
name|ReflectionUtil
import|;
end_import

begin_class
specifier|public
class|class
name|BeanTypeInfo
block|{
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
name|mappedName2typeName
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|mappedName2pdName
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|AegisType
argument_list|>
name|mappedName2type
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|AegisType
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
decl_stmt|;
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|PropertyDescriptor
index|[]
name|descriptors
decl_stmt|;
specifier|private
name|TypeMapping
name|typeMapping
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|initialized
decl_stmt|;
specifier|private
name|String
name|defaultNamespace
decl_stmt|;
specifier|private
name|int
name|minOccurs
decl_stmt|;
specifier|private
name|boolean
name|nillable
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|isExtension
decl_stmt|;
specifier|private
name|boolean
name|qualifyAttributes
decl_stmt|;
specifier|private
name|boolean
name|qualifyElements
init|=
literal|true
decl_stmt|;
comment|/**      * extensibleElements means adding xs:any to WSDL Complex AegisType Definition      */
specifier|private
name|boolean
name|extensibleElements
init|=
literal|true
decl_stmt|;
comment|/**      * extensibleAttributes means adding xs:anyAttribute to WSDL Complex AegisType      * Definition      */
specifier|private
name|boolean
name|extensibleAttributes
init|=
literal|true
decl_stmt|;
specifier|public
name|BeanTypeInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
name|this
operator|.
name|beanClass
operator|=
name|typeClass
expr_stmt|;
name|this
operator|.
name|defaultNamespace
operator|=
name|defaultNamespace
expr_stmt|;
name|initializeProperties
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create a BeanTypeInfo class.      *       * @param typeClass      * @param defaultNamespace      * @param initiallize If true attempt default property/xml mappings.      */
specifier|public
name|BeanTypeInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|String
name|defaultNamespace
parameter_list|,
name|boolean
name|initialize
parameter_list|)
block|{
name|this
operator|.
name|beanClass
operator|=
name|typeClass
expr_stmt|;
name|this
operator|.
name|defaultNamespace
operator|=
name|defaultNamespace
expr_stmt|;
name|initializeProperties
argument_list|()
expr_stmt|;
name|initialized
operator|=
operator|!
name|initialize
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultNamespace
parameter_list|()
block|{
return|return
name|defaultNamespace
return|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|initializeSync
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|DatabindingException
condition|)
block|{
throw|throw
operator|(
name|DatabindingException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't create TypeInfo."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|initializeSync
parameter_list|()
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|descriptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// Don't map the property unless there is a read property
if|if
condition|(
name|isMapped
argument_list|(
name|descriptors
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|mapProperty
argument_list|(
name|descriptors
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isMapped
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
if|if
condition|(
name|pd
operator|.
name|getReadMethod
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|mapProperty
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
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
name|qualifyAttributes
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
name|qualifyElements
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|PropertyDescriptor
index|[]
name|getPropertyDescriptors
parameter_list|()
block|{
return|return
name|descriptors
return|;
block|}
specifier|protected
name|PropertyDescriptor
name|getPropertyDescriptor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|descriptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|descriptors
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|descriptors
index|[
name|i
index|]
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Get the type class for the field with the specified QName.      */
specifier|public
name|AegisType
name|getType
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
comment|// 1. Try a prexisting mapped type
name|AegisType
name|type
init|=
name|mappedName2type
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// 2. Try to get the type by its name, if there is one
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|QName
name|typeName
init|=
name|getMappedTypeName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeName
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|mapType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// 3. Create the type from the property descriptor and map it
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|PropertyDescriptor
name|desc
decl_stmt|;
try|try
block|{
name|desc
operator|=
name|getPropertyDescriptorFromMappedName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|DatabindingException
condition|)
block|{
throw|throw
operator|(
name|DatabindingException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't get properties."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|desc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|TypeMapping
name|tm
init|=
name|getTypeMapping
argument_list|()
decl_stmt|;
name|TypeCreator
name|tc
init|=
name|tm
operator|.
name|getTypeCreator
argument_list|()
decl_stmt|;
name|type
operator|=
name|tc
operator|.
name|createType
argument_list|(
name|desc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DatabindingException
name|e
parameter_list|)
block|{
name|e
operator|.
name|prepend
argument_list|(
literal|"Couldn't create type for property "
operator|+
name|desc
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
name|e
throw|;
block|}
comment|// second part is possible workaround for XFIRE-586
if|if
condition|(
name|registerType
argument_list|(
name|desc
argument_list|)
condition|)
block|{
name|getTypeMapping
argument_list|()
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|mapType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't find type for property "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|type
return|;
block|}
specifier|protected
name|boolean
name|registerType
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|mapType
parameter_list|(
name|QName
name|name
parameter_list|,
name|AegisType
name|type
parameter_list|)
block|{
name|mappedName2type
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|private
name|QName
name|getMappedTypeName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|mappedName2typeName
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|typeMapping
return|;
block|}
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
comment|/**      * Specifies the name of the property as it shows up in the xml schema. This      * method just returns<code>propertyDescriptor.getName();</code>      *       * @param desc      * @return      */
specifier|protected
name|QName
name|createMappedName
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|,
name|boolean
name|qualified
parameter_list|)
block|{
if|if
condition|(
name|qualified
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|getDefaultNamespace
argument_list|()
argument_list|,
name|desc
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|QName
argument_list|(
literal|null
argument_list|,
name|desc
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|mapAttribute
parameter_list|(
name|String
name|property
parameter_list|,
name|QName
name|mappedName
parameter_list|)
block|{
name|mappedName2pdName
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|mappedName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|mapElement
parameter_list|(
name|String
name|property
parameter_list|,
name|QName
name|mappedName
parameter_list|)
block|{
name|mappedName2pdName
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|mappedName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies the SchemaType for a particular class.      *       * @param mappedName      * @param type      */
specifier|public
name|void
name|mapTypeName
parameter_list|(
name|QName
name|mappedName
parameter_list|,
name|QName
name|type
parameter_list|)
block|{
name|mappedName2typeName
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initializeProperties
parameter_list|()
block|{
name|BeanInfo
name|beanInfo
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|beanClass
operator|.
name|isInterface
argument_list|()
operator|||
name|beanClass
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|descriptors
operator|=
name|getInterfacePropertyDescriptors
argument_list|(
name|beanClass
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanClass
operator|.
name|isEnum
argument_list|()
condition|)
block|{
comment|// do nothing
block|}
elseif|else
if|if
condition|(
name|beanClass
operator|==
name|Object
operator|.
name|class
operator|||
name|beanClass
operator|==
name|Throwable
operator|.
name|class
condition|)
block|{
comment|// do nothing
block|}
elseif|else
if|if
condition|(
name|beanClass
operator|==
name|Throwable
operator|.
name|class
condition|)
block|{
comment|// do nothing
block|}
elseif|else
if|if
condition|(
name|Throwable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|beanClass
argument_list|)
condition|)
block|{
name|beanInfo
operator|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RuntimeException
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|beanClass
argument_list|)
condition|)
block|{
name|beanInfo
operator|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|,
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Throwable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|beanClass
argument_list|)
condition|)
block|{
name|beanInfo
operator|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|beanInfo
operator|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IntrospectionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't introspect interface."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|beanInfo
operator|!=
literal|null
condition|)
block|{
name|PropertyDescriptor
index|[]
name|propertyDescriptors
init|=
name|beanInfo
operator|.
name|getPropertyDescriptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyDescriptors
operator|!=
literal|null
condition|)
block|{
comment|// see comments on this function.
name|descriptors
operator|=
name|ReflectionUtil
operator|.
name|getPropertyDescriptorsAvoidSunBug
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|beanInfo
argument_list|,
name|beanClass
argument_list|,
name|propertyDescriptors
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|descriptors
operator|==
literal|null
condition|)
block|{
name|descriptors
operator|=
operator|new
name|PropertyDescriptor
index|[
literal|0
index|]
expr_stmt|;
block|}
name|Arrays
operator|.
name|sort
argument_list|(
name|descriptors
argument_list|,
operator|new
name|Comparator
argument_list|<
name|PropertyDescriptor
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|PropertyDescriptor
name|o1
parameter_list|,
name|PropertyDescriptor
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|PropertyDescriptor
index|[]
name|getInterfacePropertyDescriptors
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|List
argument_list|<
name|PropertyDescriptor
argument_list|>
name|pds
init|=
operator|new
name|ArrayList
argument_list|<
name|PropertyDescriptor
argument_list|>
argument_list|()
decl_stmt|;
name|getInterfacePropertyDescriptors
argument_list|(
name|clazz
argument_list|,
name|pds
argument_list|,
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pds
operator|.
name|toArray
argument_list|(
operator|new
name|PropertyDescriptor
index|[
name|pds
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|getInterfacePropertyDescriptors
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|List
argument_list|<
name|PropertyDescriptor
argument_list|>
name|pds
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
if|if
condition|(
name|classes
operator|.
name|contains
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return;
block|}
name|classes
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|clazz
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
comment|/**              * add base interface information              */
name|BeanInfo
name|info
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|info
operator|.
name|getPropertyDescriptors
argument_list|()
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|PropertyDescriptor
name|pd
init|=
name|info
operator|.
name|getPropertyDescriptors
argument_list|()
index|[
name|j
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|containsPropertyName
argument_list|(
name|pds
argument_list|,
name|pd
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|pds
operator|.
name|add
argument_list|(
name|pd
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**              * add extended interface information              */
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|interfaces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|getInterfacePropertyDescriptors
argument_list|(
name|interfaces
index|[
name|i
index|]
argument_list|,
name|pds
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IntrospectionException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
specifier|private
name|boolean
name|containsPropertyName
parameter_list|(
name|List
argument_list|<
name|PropertyDescriptor
argument_list|>
name|pds
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|PropertyDescriptor
argument_list|>
name|itr
init|=
name|pds
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|PropertyDescriptor
name|pd
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pd
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|PropertyDescriptor
name|getPropertyDescriptorFromMappedName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|getPropertyDescriptor
argument_list|(
name|getPropertyNameFromMappedName
argument_list|(
name|name
argument_list|)
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
literal|false
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
literal|true
return|;
block|}
specifier|protected
name|boolean
name|isSerializable
parameter_list|(
name|PropertyDescriptor
name|desc
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeClass
parameter_list|()
block|{
return|return
name|beanClass
return|;
block|}
comment|/**      * Nillable is only allowed if the actual property is Nullable      *       * @param name      * @return      */
specifier|public
name|boolean
name|isNillable
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|AegisType
name|type
init|=
name|getType
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|isNillable
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|nillable
return|;
block|}
comment|/**      * Return the minOccurs value. When there is no XML file or annotation (the situation      * if we are running from the base class here), there is no source for the       * minOccurs parameter except the default, which is supplied from the overall Aegis options.      * @param name Element QName      * @return      */
specifier|public
name|int
name|getMinOccurs
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|minOccurs
return|;
block|}
comment|/**      * Return the maxOccurs value. When there is no XML file or annotation (the situation      * if we are in the base class here), there is no per-element source for this item,      * and the value is always 1.      * @param name Element QName      * @return 1      */
specifier|public
name|int
name|getMaxOccurs
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
literal|1
return|;
block|}
specifier|public
name|long
name|getMinOccurs
parameter_list|()
block|{
return|return
name|minOccurs
return|;
block|}
specifier|public
name|void
name|setDefaultMinOccurs
parameter_list|(
name|int
name|m
parameter_list|)
block|{
name|this
operator|.
name|minOccurs
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultNillable
parameter_list|(
name|boolean
name|n
parameter_list|)
block|{
name|this
operator|.
name|nillable
operator|=
name|n
expr_stmt|;
block|}
specifier|private
name|String
name|getPropertyNameFromMappedName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|mappedName2pdName
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|QName
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
specifier|public
name|List
argument_list|<
name|QName
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
name|elements
return|;
block|}
specifier|public
name|boolean
name|isExtensibleElements
parameter_list|()
block|{
return|return
name|extensibleElements
return|;
block|}
specifier|public
name|void
name|setExtensibleElements
parameter_list|(
name|boolean
name|futureProof
parameter_list|)
block|{
name|this
operator|.
name|extensibleElements
operator|=
name|futureProof
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExtensibleAttributes
parameter_list|()
block|{
return|return
name|extensibleAttributes
return|;
block|}
specifier|public
name|void
name|setExtensibleAttributes
parameter_list|(
name|boolean
name|extensibleAttributes
parameter_list|)
block|{
name|this
operator|.
name|extensibleAttributes
operator|=
name|extensibleAttributes
expr_stmt|;
block|}
specifier|public
name|void
name|setExtension
parameter_list|(
name|boolean
name|extension
parameter_list|)
block|{
name|this
operator|.
name|isExtension
operator|=
name|extension
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExtension
parameter_list|()
block|{
return|return
name|isExtension
return|;
block|}
comment|/** * @return Returns the qualifyAttributes.      */
specifier|public
name|boolean
name|isQualifyAttributes
parameter_list|()
block|{
return|return
name|qualifyAttributes
return|;
block|}
comment|/**      * @param qualifyAttributes The qualifyAttributes to set.      */
specifier|public
name|void
name|setQualifyAttributes
parameter_list|(
name|boolean
name|qualifyAttributes
parameter_list|)
block|{
name|this
operator|.
name|qualifyAttributes
operator|=
name|qualifyAttributes
expr_stmt|;
block|}
comment|/** * @return Returns the qualifyElements.      */
specifier|public
name|boolean
name|isQualifyElements
parameter_list|()
block|{
return|return
name|qualifyElements
return|;
block|}
comment|/**      * @param qualifyElements The qualifyElements to set.      */
specifier|public
name|void
name|setQualifyElements
parameter_list|(
name|boolean
name|qualifyElements
parameter_list|)
block|{
name|this
operator|.
name|qualifyElements
operator|=
name|qualifyElements
expr_stmt|;
block|}
block|}
end_class

end_unit

