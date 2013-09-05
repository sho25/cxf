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
operator|.
name|visitor
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
name|Type
import|;
end_import

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
name|PrimitiveStatement
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
name|SearchConditionVisitor
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
name|ext
operator|.
name|search
operator|.
name|collections
operator|.
name|CollectionCheckStatement
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSearchConditionVisitor
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
implements|implements
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|primitiveFieldTypeMap
decl_stmt|;
specifier|private
name|PropertyValidator
argument_list|<
name|Object
argument_list|>
name|validator
decl_stmt|;
specifier|private
name|boolean
name|wildcardStringMatch
decl_stmt|;
specifier|protected
name|AbstractSearchConditionVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|this
operator|.
name|fieldMap
operator|=
name|fieldMap
expr_stmt|;
block|}
specifier|protected
name|String
name|getRealPropertyName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|fieldMap
operator|!=
literal|null
operator|&&
name|fieldMap
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|fieldMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
name|name
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getPrimitiveFieldClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueCls
parameter_list|)
block|{
return|return
name|getPrimitiveFieldClass
argument_list|(
name|name
argument_list|,
name|valueCls
argument_list|,
literal|null
argument_list|)
operator|.
name|getCls
argument_list|()
return|;
block|}
specifier|protected
name|ClassValue
name|getPrimitiveFieldClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueCls
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|getPrimitiveFieldClass
argument_list|(
literal|null
argument_list|,
name|name
argument_list|,
name|valueCls
argument_list|,
name|valueCls
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|protected
name|ClassValue
name|getPrimitiveFieldClass
parameter_list|(
name|PrimitiveStatement
name|ps
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueCls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|doGetPrimitiveFieldClass
argument_list|(
name|ps
argument_list|,
name|name
argument_list|,
name|valueCls
argument_list|,
name|type
argument_list|,
name|value
argument_list|,
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|ClassValue
name|doGetPrimitiveFieldClass
parameter_list|(
name|PrimitiveStatement
name|ps
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueCls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Object
name|value
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
name|boolean
name|isCollection
init|=
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|valueCls
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|actualCls
init|=
name|isCollection
condition|?
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|type
argument_list|)
else|:
name|valueCls
decl_stmt|;
name|CollectionCheckInfo
name|collInfo
init|=
literal|null
decl_stmt|;
name|int
name|index
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"."
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
name|String
index|[]
name|names
init|=
name|name
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|actualCls
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|nextPart
init|=
name|names
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|nextPart
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|nextPart
operator|=
name|nextPart
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|nextPart
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|nextPart
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|nextPart
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|Method
name|m
init|=
name|actualCls
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
if|if
condition|(
name|isCollection
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|Collection
operator|)
name|value
operator|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|value
operator|=
name|m
operator|.
name|invoke
argument_list|(
name|value
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
expr_stmt|;
name|valueCls
operator|=
name|value
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|type
operator|=
name|m
operator|.
name|getGenericReturnType
argument_list|()
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
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|doGetPrimitiveFieldClass
argument_list|(
name|ps
argument_list|,
name|name
argument_list|,
name|valueCls
argument_list|,
name|type
argument_list|,
name|value
argument_list|,
name|set
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|isCollection
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|Collection
name|coll
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
name|value
operator|=
name|coll
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|coll
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|valueCls
operator|=
name|actualCls
expr_stmt|;
if|if
condition|(
name|ps
operator|instanceof
name|CollectionCheckStatement
condition|)
block|{
name|collInfo
operator|=
operator|(
operator|(
name|CollectionCheckStatement
operator|)
name|ps
operator|)
operator|.
name|getCollectionCheckInfo
argument_list|()
expr_stmt|;
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|primitiveFieldTypeMap
operator|!=
literal|null
condition|)
block|{
name|cls
operator|=
name|primitiveFieldTypeMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|cls
operator|=
name|valueCls
expr_stmt|;
block|}
return|return
operator|new
name|ClassValue
argument_list|(
name|cls
argument_list|,
name|value
argument_list|,
name|collInfo
argument_list|,
name|set
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPrimitiveFieldTypeMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|primitiveFieldTypeMap
parameter_list|)
block|{
name|this
operator|.
name|primitiveFieldTypeMap
operator|=
name|primitiveFieldTypeMap
expr_stmt|;
block|}
specifier|public
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|visitor
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|protected
class|class
name|ClassValue
block|{
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
specifier|private
name|Object
name|value
decl_stmt|;
specifier|private
name|CollectionCheckInfo
name|collInfo
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|collectionProps
decl_stmt|;
specifier|public
name|ClassValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|value
parameter_list|,
name|CollectionCheckInfo
name|collInfo
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|collectionProps
parameter_list|)
block|{
name|this
operator|.
name|cls
operator|=
name|cls
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|collInfo
operator|=
name|collInfo
expr_stmt|;
name|this
operator|.
name|collectionProps
operator|=
name|collectionProps
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getCls
parameter_list|()
block|{
return|return
name|cls
return|;
block|}
specifier|public
name|void
name|setCls
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|this
operator|.
name|cls
operator|=
name|cls
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|CollectionCheckInfo
name|getCollectionCheckInfo
parameter_list|()
block|{
return|return
name|collInfo
return|;
block|}
specifier|public
name|boolean
name|isCollection
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|collectionProps
operator|!=
literal|null
operator|&&
name|collectionProps
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
specifier|protected
name|void
name|validatePropertyValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|validate
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setValidator
parameter_list|(
name|PropertyValidator
argument_list|<
name|Object
argument_list|>
name|validator
parameter_list|)
block|{
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWildcardStringMatch
parameter_list|()
block|{
return|return
name|wildcardStringMatch
return|;
block|}
specifier|public
name|void
name|setWildcardStringMatch
parameter_list|(
name|boolean
name|wildcardStringMatch
parameter_list|)
block|{
name|this
operator|.
name|wildcardStringMatch
operator|=
name|wildcardStringMatch
expr_stmt|;
block|}
block|}
end_class

end_unit

