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
name|databinding
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
name|interceptor
operator|.
name|Fault
import|;
end_import

begin_comment
comment|/**  *  This wrapper helper will use reflection to handle the wrapped message  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractWrapperHelper
implements|implements
name|WrapperHelper
block|{
specifier|public
specifier|static
specifier|final
name|Class
name|NO_CLASSES
index|[]
init|=
operator|new
name|Class
index|[
literal|0
index|]
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Object
name|NO_PARAMS
index|[]
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
specifier|protected
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
decl_stmt|;
specifier|protected
specifier|final
name|Method
name|setMethods
index|[]
decl_stmt|;
specifier|protected
specifier|final
name|Method
name|getMethods
index|[]
decl_stmt|;
specifier|protected
specifier|final
name|Field
name|fields
index|[]
decl_stmt|;
specifier|protected
name|AbstractWrapperHelper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|wt
parameter_list|,
name|Method
name|sets
index|[]
parameter_list|,
name|Method
name|gets
index|[]
parameter_list|,
name|Field
name|f
index|[]
parameter_list|)
block|{
name|setMethods
operator|=
name|sets
expr_stmt|;
name|getMethods
operator|=
name|gets
expr_stmt|;
name|fields
operator|=
name|f
expr_stmt|;
name|wrapperType
operator|=
name|wt
expr_stmt|;
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
specifier|protected
specifier|abstract
name|Object
name|createWrapperObject
parameter_list|(
name|Class
name|typeClass
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|protected
specifier|abstract
name|Object
name|getWrapperObject
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|protected
name|Object
name|getPartObject
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|object
return|;
block|}
specifier|protected
name|Object
name|getValue
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|in
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|in
argument_list|)
return|;
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
try|try
block|{
name|Object
name|wrapperObject
init|=
name|createWrapperObject
argument_list|(
name|wrapperType
argument_list|)
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
name|setMethods
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|getMethods
index|[
name|x
index|]
operator|==
literal|null
operator|&&
name|setMethods
index|[
name|x
index|]
operator|==
literal|null
operator|&&
name|fields
index|[
name|x
index|]
operator|==
literal|null
condition|)
block|{
comment|//this part is a header or something
comment|//that is not part of the wrapper.
continue|continue;
block|}
name|Object
name|o
init|=
name|lst
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|o
operator|=
name|getPartObject
argument_list|(
name|x
argument_list|,
name|o
argument_list|)
expr_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|List
operator|&&
name|getMethods
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|col
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|getMethods
index|[
name|x
index|]
operator|.
name|invoke
argument_list|(
name|wrapperObject
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|==
literal|null
condition|)
block|{
comment|//broken generated java wrappers
if|if
condition|(
name|setMethods
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|setMethods
index|[
name|x
index|]
operator|.
name|invoke
argument_list|(
name|wrapperObject
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fields
index|[
name|x
index|]
operator|.
name|set
argument_list|(
name|wrapperObject
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
block|}
else|else
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|olst
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|o
argument_list|)
decl_stmt|;
name|col
operator|.
name|addAll
argument_list|(
name|olst
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|setMethods
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|setMethods
index|[
name|x
index|]
operator|.
name|invoke
argument_list|(
name|wrapperObject
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fields
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|fields
index|[
name|x
index|]
operator|.
name|set
argument_list|(
name|wrapperObject
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
block|}
return|return
name|wrapperObject
return|;
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
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
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
try|try
block|{
name|Object
name|wrapperObject
init|=
name|getWrapperObject
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|getMethods
operator|.
name|length
argument_list|)
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
name|getMethods
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|getMethods
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|getValue
argument_list|(
name|getMethods
index|[
name|x
index|]
argument_list|,
name|wrapperObject
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fields
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|fields
index|[
name|x
index|]
operator|.
name|get
argument_list|(
name|wrapperObject
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//placeholder
name|ret
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

