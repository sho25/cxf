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
name|ext
operator|.
name|search
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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Time
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|datatype
operator|.
name|DatatypeConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeFactory
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
name|PropertyUtils
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
name|ext
operator|.
name|search
operator|.
name|Beanspector
operator|.
name|TypeInfo
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
name|ext
operator|.
name|search
operator|.
name|collections
operator|.
name|CollectionCheck
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
name|ext
operator|.
name|search
operator|.
name|collections
operator|.
name|CollectionCheckInfo
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
name|provider
operator|.
name|ServerProviderFactory
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
name|InjectionUtils
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
name|message
operator|.
name|MessageUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSearchConditionParser
parameter_list|<
name|T
parameter_list|>
implements|implements
name|SearchConditionParser
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Annotation
index|[]
name|EMPTY_ANNOTTAIONS
init|=
operator|new
name|Annotation
index|[]
block|{}
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|contextProperties
decl_stmt|;
specifier|protected
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|conditionClass
decl_stmt|;
specifier|protected
name|Beanspector
argument_list|<
name|T
argument_list|>
name|beanspector
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanPropertiesMap
decl_stmt|;
specifier|protected
name|AbstractSearchConditionParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|tclass
parameter_list|)
block|{
name|this
argument_list|(
name|tclass
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractSearchConditionParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|tclass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|contextProperties
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|)
block|{
name|this
operator|.
name|conditionClass
operator|=
name|tclass
expr_stmt|;
name|this
operator|.
name|contextProperties
operator|=
name|contextProperties
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|String
operator|,
name|String
operator|>
name|emptyMap
argument_list|()
operator|:
name|contextProperties
expr_stmt|;
name|beanspector
operator|=
name|SearchBean
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|tclass
argument_list|)
condition|?
literal|null
else|:
operator|new
name|Beanspector
argument_list|<
name|T
argument_list|>
argument_list|(
name|tclass
argument_list|)
expr_stmt|;
name|this
operator|.
name|beanPropertiesMap
operator|=
name|beanProperties
expr_stmt|;
block|}
specifier|protected
name|String
name|getActualSetterName
parameter_list|(
name|String
name|setter
parameter_list|)
block|{
name|String
name|beanPropertyName
init|=
name|beanPropertiesMap
operator|==
literal|null
condition|?
literal|null
else|:
name|beanPropertiesMap
operator|.
name|get
argument_list|(
name|setter
argument_list|)
decl_stmt|;
return|return
name|beanPropertyName
operator|!=
literal|null
condition|?
name|beanPropertyName
else|:
name|setter
return|;
block|}
specifier|protected
name|Boolean
name|isDecodeQueryValues
parameter_list|()
block|{
return|return
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|contextProperties
operator|.
name|get
argument_list|(
name|SearchUtils
operator|.
name|DECODE_QUERY_VALUES
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|TypeInfo
name|getTypeInfo
parameter_list|(
name|String
name|setter
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SearchParseException
throws|,
name|PropertyNotFoundException
block|{
name|String
name|name
init|=
name|getSetter
argument_list|(
name|setter
argument_list|)
decl_stmt|;
name|TypeInfo
name|typeInfo
init|=
literal|null
decl_stmt|;
try|try
block|{
name|typeInfo
operator|=
name|beanspector
operator|!=
literal|null
condition|?
name|beanspector
operator|.
name|getAccessorTypeInfo
argument_list|(
name|name
argument_list|)
else|:
operator|new
name|TypeInfo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// continue
block|}
if|if
condition|(
name|typeInfo
operator|==
literal|null
operator|&&
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|contextProperties
operator|.
name|get
argument_list|(
name|SearchUtils
operator|.
name|LAX_PROPERTY_MATCH
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|PropertyNotFoundException
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
throw|;
block|}
return|return
name|typeInfo
return|;
block|}
specifier|protected
name|String
name|getSetter
parameter_list|(
name|String
name|setter
parameter_list|)
block|{
name|int
name|index
init|=
name|getDotIndex
argument_list|(
name|setter
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|setter
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|setter
return|;
block|}
block|}
specifier|protected
name|Object
name|parseType
parameter_list|(
name|String
name|originalPropName
parameter_list|,
name|Object
name|ownerBean
parameter_list|,
name|Object
name|lastCastedValue
parameter_list|,
name|String
name|setter
parameter_list|,
name|TypeInfo
name|typeInfo
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SearchParseException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
init|=
name|typeInfo
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|boolean
name|isCollection
init|=
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|valueType
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|actualType
init|=
name|isCollection
condition|?
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|typeInfo
operator|.
name|getGenericType
argument_list|()
argument_list|)
else|:
name|valueType
decl_stmt|;
name|int
name|index
init|=
name|getDotIndex
argument_list|(
name|setter
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|Object
name|castedValue
init|=
name|value
decl_stmt|;
if|if
condition|(
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|valueType
argument_list|)
condition|)
block|{
name|castedValue
operator|=
name|convertToDate
argument_list|(
name|valueType
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|isPrimitive
init|=
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|valueType
argument_list|)
decl_stmt|;
name|boolean
name|isPrimitiveOrEnum
init|=
name|isPrimitive
operator|||
name|valueType
operator|.
name|isEnum
argument_list|()
decl_stmt|;
if|if
condition|(
name|ownerBean
operator|==
literal|null
operator|||
name|isPrimitiveOrEnum
condition|)
block|{
try|try
block|{
name|CollectionCheck
name|collCheck
init|=
name|getCollectionCheck
argument_list|(
name|originalPropName
argument_list|,
name|isCollection
argument_list|,
name|actualType
argument_list|)
decl_stmt|;
if|if
condition|(
name|collCheck
operator|==
literal|null
condition|)
block|{
name|castedValue
operator|=
name|InjectionUtils
operator|.
name|convertStringToPrimitive
argument_list|(
name|value
argument_list|,
name|actualType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|collCheck
operator|==
literal|null
operator|&&
name|isCollection
condition|)
block|{
name|castedValue
operator|=
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|castedValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isCollection
condition|)
block|{
name|typeInfo
operator|.
name|setCollectionCheckInfo
argument_list|(
operator|new
name|CollectionCheckInfo
argument_list|(
name|collCheck
argument_list|,
name|castedValue
argument_list|)
argument_list|)
expr_stmt|;
name|castedValue
operator|=
name|getEmptyCollection
argument_list|(
name|valueType
argument_list|)
expr_stmt|;
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
name|SearchParseException
argument_list|(
literal|"Cannot convert String value \""
operator|+
name|value
operator|+
literal|"\" to a value of class "
operator|+
name|valueType
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|classType
init|=
name|isCollection
condition|?
name|valueType
else|:
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
try|try
block|{
name|Method
name|setterM
init|=
name|valueType
operator|.
name|getMethod
argument_list|(
literal|"set"
operator|+
name|getMethodNameSuffix
argument_list|(
name|setter
argument_list|)
argument_list|,
operator|new
name|Class
index|[]
block|{
name|classType
block|}
argument_list|)
decl_stmt|;
name|Object
name|objectValue
init|=
operator|!
name|isCollection
condition|?
name|value
else|:
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setterM
operator|.
name|invoke
argument_list|(
name|ownerBean
argument_list|,
operator|new
name|Object
index|[]
block|{
name|objectValue
block|}
argument_list|)
expr_stmt|;
name|castedValue
operator|=
name|objectValue
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
literal|"Cannot convert String value \""
operator|+
name|value
operator|+
literal|"\" to a value of class "
operator|+
name|valueType
operator|.
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|lastCastedValue
operator|!=
literal|null
condition|)
block|{
name|castedValue
operator|=
name|lastCastedValue
expr_stmt|;
block|}
return|return
name|castedValue
return|;
block|}
else|else
block|{
name|String
index|[]
name|names
init|=
name|setter
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|nextPart
init|=
name|getMethodNameSuffix
argument_list|(
name|names
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|Method
name|getterM
init|=
name|actualType
operator|.
name|getMethod
argument_list|(
literal|"get"
operator|+
name|nextPart
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|getterM
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|boolean
name|returnCollection
init|=
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|returnType
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|actualReturnType
init|=
operator|!
name|returnCollection
condition|?
name|returnType
else|:
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|getterM
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|isPrimitive
init|=
operator|!
name|returnCollection
operator|&&
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|returnType
argument_list|)
operator|||
name|returnType
operator|.
name|isEnum
argument_list|()
decl_stmt|;
name|boolean
name|lastTry
init|=
name|names
operator|.
name|length
operator|==
literal|2
operator|&&
operator|(
name|isPrimitive
operator|||
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|returnType
argument_list|)
operator|||
name|returnCollection
operator|||
name|paramConverterAvailable
argument_list|(
name|returnType
argument_list|)
operator|)
decl_stmt|;
name|Object
name|valueObject
init|=
name|ownerBean
operator|!=
literal|null
condition|?
name|ownerBean
else|:
name|actualType
operator|.
name|isInterface
argument_list|()
condition|?
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|actualType
block|}
argument_list|,
operator|new
name|InterfaceProxy
argument_list|()
argument_list|)
else|:
name|actualType
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Object
name|nextObject
decl_stmt|;
if|if
condition|(
name|lastTry
condition|)
block|{
if|if
condition|(
operator|!
name|returnCollection
condition|)
block|{
name|nextObject
operator|=
name|isPrimitive
condition|?
name|InjectionUtils
operator|.
name|convertStringToPrimitive
argument_list|(
name|value
argument_list|,
name|returnType
argument_list|)
else|:
name|convertToDate
argument_list|(
name|returnType
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CollectionCheck
name|collCheck
init|=
name|getCollectionCheck
argument_list|(
name|originalPropName
argument_list|,
literal|true
argument_list|,
name|actualReturnType
argument_list|)
decl_stmt|;
if|if
condition|(
name|collCheck
operator|==
literal|null
condition|)
block|{
name|nextObject
operator|=
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|typeInfo
operator|.
name|setCollectionCheckInfo
argument_list|(
operator|new
name|CollectionCheckInfo
argument_list|(
name|collCheck
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|nextObject
operator|=
name|getEmptyCollection
argument_list|(
name|valueType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|returnCollection
condition|)
block|{
name|nextObject
operator|=
name|returnType
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|nextObject
operator|=
name|actualReturnType
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
name|Method
name|setterM
init|=
name|actualType
operator|.
name|getMethod
argument_list|(
literal|"set"
operator|+
name|nextPart
argument_list|,
operator|new
name|Class
index|[]
block|{
name|returnType
block|}
argument_list|)
decl_stmt|;
name|Object
name|valueObjectValue
init|=
name|lastTry
operator|||
operator|!
name|returnCollection
condition|?
name|nextObject
else|:
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|nextObject
argument_list|)
decl_stmt|;
name|setterM
operator|.
name|invoke
argument_list|(
name|valueObject
argument_list|,
operator|new
name|Object
index|[]
block|{
name|valueObjectValue
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|lastTry
condition|)
block|{
name|lastCastedValue
operator|=
name|lastCastedValue
operator|==
literal|null
condition|?
name|valueObject
else|:
name|lastCastedValue
expr_stmt|;
return|return
name|isCollection
condition|?
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|lastCastedValue
argument_list|)
else|:
name|lastCastedValue
return|;
block|}
else|else
block|{
name|lastCastedValue
operator|=
name|valueObject
expr_stmt|;
block|}
name|TypeInfo
name|nextTypeInfo
init|=
operator|new
name|TypeInfo
argument_list|(
name|valueObjectValue
operator|.
name|getClass
argument_list|()
argument_list|,
name|getterM
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|response
init|=
name|parseType
argument_list|(
name|originalPropName
argument_list|,
name|nextObject
argument_list|,
name|lastCastedValue
argument_list|,
name|setter
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|,
name|nextTypeInfo
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|ownerBean
operator|==
literal|null
condition|)
block|{
return|return
name|isCollection
condition|?
name|getCollectionSingleton
argument_list|(
name|valueType
argument_list|,
name|lastCastedValue
argument_list|)
else|:
name|lastCastedValue
return|;
block|}
else|else
block|{
return|return
name|response
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
literal|"Cannot convert String value \""
operator|+
name|value
operator|+
literal|"\" to a value of class "
operator|+
name|valueType
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|boolean
name|paramConverterAvailable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|pClass
parameter_list|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|ServerProviderFactory
name|pf
init|=
name|m
operator|==
literal|null
condition|?
literal|null
else|:
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|m
argument_list|)
decl_stmt|;
return|return
name|pf
operator|!=
literal|null
operator|&&
name|pf
operator|.
name|createParameterHandler
argument_list|(
name|pClass
argument_list|,
name|pClass
argument_list|,
name|EMPTY_ANNOTTAIONS
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|CollectionCheck
name|getCollectionCheck
parameter_list|(
name|String
name|propName
parameter_list|,
name|boolean
name|isCollection
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|actualCls
parameter_list|)
block|{
if|if
condition|(
name|isCollection
condition|)
block|{
if|if
condition|(
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|actualCls
argument_list|)
condition|)
block|{
if|if
condition|(
name|isCount
argument_list|(
name|propName
argument_list|)
condition|)
block|{
return|return
name|CollectionCheck
operator|.
name|SIZE
return|;
block|}
block|}
else|else
block|{
return|return
name|CollectionCheck
operator|.
name|SIZE
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|boolean
name|isCount
parameter_list|(
name|String
name|propName
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|private
name|Object
name|getCollectionSingleton
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|collectionCls
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|Set
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|collectionCls
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
specifier|private
name|Object
name|getEmptyCollection
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|collectionCls
parameter_list|)
block|{
if|if
condition|(
name|Set
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|collectionCls
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
specifier|private
name|Object
name|convertToDate
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SearchParseException
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Object
name|obj
init|=
name|InjectionUtils
operator|.
name|createFromParameterHandler
argument_list|(
name|value
argument_list|,
name|valueType
argument_list|,
name|valueType
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
return|return
name|obj
return|;
block|}
try|try
block|{
if|if
condition|(
name|Timestamp
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|valueType
argument_list|)
condition|)
block|{
return|return
name|convertToTimestamp
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Time
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|valueType
argument_list|)
condition|)
block|{
return|return
name|convertToTime
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|convertToDefaultDate
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
comment|// is that duration?
try|try
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
name|value
argument_list|)
operator|.
name|addTo
argument_list|(
name|now
argument_list|)
expr_stmt|;
return|return
name|now
return|;
block|}
catch|catch
parameter_list|(
name|DatatypeConfigurationException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
literal|"Can parse "
operator|+
name|value
operator|+
literal|" neither as date nor duration"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|Timestamp
name|convertToTimestamp
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|ParseException
block|{
name|Date
name|date
init|=
name|convertToDefaultDate
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|Timestamp
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Time
name|convertToTime
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|ParseException
block|{
name|Date
name|date
init|=
name|convertToDefaultDate
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|Time
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Date
name|convertToDefaultDate
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|ParseException
block|{
name|DateFormat
name|df
init|=
name|SearchUtils
operator|.
name|getDateFormat
argument_list|(
name|contextProperties
argument_list|)
decl_stmt|;
name|String
name|dateValue
init|=
name|value
decl_stmt|;
if|if
condition|(
name|SearchUtils
operator|.
name|isTimeZoneSupported
argument_list|(
name|contextProperties
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
condition|)
block|{
comment|// zone in XML is "+01:00" in Java is "+0100"; stripping semicolon
name|int
name|idx
init|=
name|value
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|dateValue
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
operator|+
name|value
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|df
operator|.
name|parse
argument_list|(
name|dateValue
argument_list|)
return|;
block|}
specifier|private
name|String
name|getMethodNameSuffix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
specifier|private
name|int
name|getDotIndex
parameter_list|(
name|String
name|setter
parameter_list|)
block|{
return|return
name|this
operator|.
name|conditionClass
operator|==
name|SearchBean
operator|.
name|class
condition|?
operator|-
literal|1
else|:
name|setter
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
return|;
block|}
block|}
end_class

end_unit

