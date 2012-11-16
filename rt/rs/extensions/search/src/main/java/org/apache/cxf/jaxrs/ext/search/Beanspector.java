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
name|beans
operator|.
name|IntrospectionException
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
name|InvocationTargetException
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
name|Type
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * Bean introspection utility.  */
end_comment

begin_class
specifier|public
class|class
name|Beanspector
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|tclass
decl_stmt|;
specifier|private
name|T
name|tobj
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|getters
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|setters
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Beanspector
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|tclass
parameter_list|)
block|{
if|if
condition|(
name|tclass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"tclass is null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|tclass
operator|=
name|tclass
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Beanspector
parameter_list|(
name|T
name|tobj
parameter_list|)
block|{
if|if
condition|(
name|tobj
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"tobj is null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|tobj
operator|=
name|tobj
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|tclass
operator|==
literal|null
condition|)
block|{
name|tclass
operator|=
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|tobj
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Method
name|m
range|:
name|tclass
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isGetter
argument_list|(
name|m
argument_list|)
condition|)
block|{
name|getters
operator|.
name|put
argument_list|(
name|getPropertyName
argument_list|(
name|m
argument_list|)
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isSetter
argument_list|(
name|m
argument_list|)
condition|)
block|{
name|setters
operator|.
name|put
argument_list|(
name|getPropertyName
argument_list|(
name|m
argument_list|)
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check type equality for getter-setter pairs
name|Set
argument_list|<
name|String
argument_list|>
name|pairs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|getters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|pairs
operator|.
name|retainAll
argument_list|(
name|setters
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|accessor
range|:
name|pairs
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|getterClass
init|=
name|getters
operator|.
name|get
argument_list|(
name|accessor
argument_list|)
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|setterClass
init|=
name|setters
operator|.
name|get
argument_list|(
name|accessor
argument_list|)
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|getterClass
operator|.
name|equals
argument_list|(
name|setterClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Accessor '%s' type mismatch, getter type is %s while setter type is %s"
argument_list|,
name|accessor
argument_list|,
name|getterClass
operator|.
name|getName
argument_list|()
argument_list|,
name|setterClass
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|T
name|getBean
parameter_list|()
block|{
return|return
name|tobj
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getGettersNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|getters
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getSettersNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|setters
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|TypeInfo
name|getAccessorTypeInfo
parameter_list|(
name|String
name|getterOrSetterName
parameter_list|)
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|getters
operator|.
name|get
argument_list|(
name|getterOrSetterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
name|setters
operator|.
name|get
argument_list|(
name|getterOrSetterName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Accessor '%s' not found, "
operator|+
literal|"known setters are: %s, known getters are: %s"
argument_list|,
name|getterOrSetterName
argument_list|,
name|setters
operator|.
name|keySet
argument_list|()
argument_list|,
name|getters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|IntrospectionException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
operator|new
name|TypeInfo
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|m
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Beanspector
argument_list|<
name|T
argument_list|>
name|swap
parameter_list|(
name|T
name|newobject
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|newobject
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"newobject is null"
argument_list|)
throw|;
block|}
name|tobj
operator|=
name|newobject
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Beanspector
argument_list|<
name|T
argument_list|>
name|instantiate
parameter_list|()
throws|throws
name|Exception
block|{
name|tobj
operator|=
name|tclass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Beanspector
argument_list|<
name|T
argument_list|>
name|setValue
parameter_list|(
name|String
name|setterName
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Throwable
block|{
name|Method
name|m
init|=
name|setters
operator|.
name|get
argument_list|(
name|setterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Setter '%s' not found, "
operator|+
literal|"known setters are: %s"
argument_list|,
name|setterName
argument_list|,
name|setters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|IntrospectionException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|setValue
argument_list|(
name|m
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Beanspector
argument_list|<
name|T
argument_list|>
name|setValue
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|settersWithValues
parameter_list|)
throws|throws
name|Throwable
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|settersWithValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|setValue
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Beanspector
argument_list|<
name|T
argument_list|>
name|setValue
parameter_list|(
name|Method
name|setter
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Throwable
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
init|=
name|setter
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
name|setter
operator|.
name|invoke
argument_list|(
name|tobj
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"; setter parameter type: %s, set value type: %s"
argument_list|,
name|paramType
operator|.
name|getName
argument_list|()
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|+
name|msg
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
specifier|public
name|Object
name|getValue
parameter_list|(
name|String
name|getterName
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
name|getValue
argument_list|(
name|getters
operator|.
name|get
argument_list|(
name|getterName
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|(
name|Method
name|getter
parameter_list|)
throws|throws
name|Throwable
block|{
try|try
block|{
return|return
name|getter
operator|.
name|invoke
argument_list|(
name|tobj
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
specifier|private
name|boolean
name|isGetter
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|&&
operator|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
operator|||
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
operator|)
return|;
block|}
specifier|private
name|String
name|getPropertyName
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
comment|// at this point the method is either getter or setter
name|String
name|result
init|=
name|m
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|result
operator|.
name|substring
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|boolean
name|isSetter
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|m
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|void
operator|.
name|class
argument_list|)
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
operator|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"set"
argument_list|)
operator|||
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
operator|)
return|;
block|}
specifier|public
specifier|static
class|class
name|TypeInfo
block|{
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
specifier|private
name|Type
name|genericType
decl_stmt|;
specifier|public
name|TypeInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|genericType
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
name|genericType
operator|=
name|genericType
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeClass
parameter_list|()
block|{
return|return
name|cls
return|;
block|}
specifier|public
name|Type
name|getGenericType
parameter_list|()
block|{
return|return
name|genericType
return|;
block|}
block|}
block|}
end_class

end_unit

