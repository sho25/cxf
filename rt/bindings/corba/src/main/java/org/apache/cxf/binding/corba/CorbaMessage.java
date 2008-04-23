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
name|binding
operator|.
name|corba
package|;
end_package

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
name|binding
operator|.
name|corba
operator|.
name|runtime
operator|.
name|CorbaStreamableImpl
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
name|binding
operator|.
name|corba
operator|.
name|types
operator|.
name|CorbaObjectHandler
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
name|AbstractWrappedMessage
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
name|omg
operator|.
name|CORBA
operator|.
name|NVList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|SystemException
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaMessage
extends|extends
name|AbstractWrappedMessage
block|{
specifier|private
name|List
argument_list|<
name|CorbaStreamable
argument_list|>
name|arguments
decl_stmt|;
specifier|private
name|CorbaStreamable
name|returnParam
decl_stmt|;
specifier|private
name|CorbaStreamable
name|except
decl_stmt|;
specifier|private
name|SystemException
name|systemExcept
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|corbaTypeMap
decl_stmt|;
specifier|private
name|NVList
name|list
decl_stmt|;
specifier|public
name|CorbaMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|instanceof
name|CorbaMessage
condition|)
block|{
name|CorbaMessage
name|msg
init|=
operator|(
name|CorbaMessage
operator|)
name|m
decl_stmt|;
name|CorbaStreamable
index|[]
name|data
init|=
name|msg
operator|.
name|getStreamableArguments
argument_list|()
decl_stmt|;
name|setStreamableArguments
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|returnParam
operator|=
name|msg
operator|.
name|getStreamableReturn
argument_list|()
expr_stmt|;
name|except
operator|=
name|msg
operator|.
name|getStreamableException
argument_list|()
expr_stmt|;
name|systemExcept
operator|=
name|msg
operator|.
name|getSystemException
argument_list|()
expr_stmt|;
name|list
operator|=
name|msg
operator|.
name|getList
argument_list|()
expr_stmt|;
name|corbaTypeMap
operator|=
name|msg
operator|.
name|getCorbaTypeMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|arguments
operator|=
operator|new
name|ArrayList
argument_list|<
name|CorbaStreamable
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setList
parameter_list|(
name|NVList
name|lst
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|lst
expr_stmt|;
block|}
specifier|public
name|NVList
name|getList
parameter_list|()
block|{
return|return
name|this
operator|.
name|list
return|;
block|}
specifier|public
name|CorbaStreamable
name|getStreamableException
parameter_list|()
block|{
return|return
name|this
operator|.
name|except
return|;
block|}
specifier|public
name|CorbaStreamable
name|getStreamableReturn
parameter_list|()
block|{
return|return
name|this
operator|.
name|returnParam
return|;
block|}
specifier|public
name|SystemException
name|getSystemException
parameter_list|()
block|{
return|return
name|this
operator|.
name|systemExcept
return|;
block|}
specifier|public
name|void
name|setSystemException
parameter_list|(
name|SystemException
name|sysEx
parameter_list|)
block|{
name|systemExcept
operator|=
name|sysEx
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|addStreamableArgument
parameter_list|(
name|CorbaStreamable
name|arg
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|arguments
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|arguments
operator|=
operator|new
name|ArrayList
argument_list|<
name|CorbaStreamable
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|arguments
operator|.
name|add
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CorbaStreamable
index|[]
name|getStreamableArguments
parameter_list|()
block|{
return|return
name|this
operator|.
name|arguments
operator|.
name|toArray
argument_list|(
operator|new
name|CorbaStreamable
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|void
name|setStreamableArguments
parameter_list|(
name|CorbaStreamable
index|[]
name|data
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|arguments
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|arguments
operator|=
operator|new
name|ArrayList
argument_list|<
name|CorbaStreamable
argument_list|>
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CorbaStreamable
name|streamable
range|:
name|data
control|)
block|{
name|addStreamableArgument
argument_list|(
name|streamable
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setStreamableArgumentValue
parameter_list|(
name|CorbaObjectHandler
name|data
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
if|if
condition|(
name|idx
operator|>=
name|this
operator|.
name|arguments
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"setStreamableArgumentValue: Index out of range"
argument_list|)
throw|;
block|}
name|this
operator|.
name|arguments
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|setObject
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStreamableArgumentValues
parameter_list|(
name|CorbaObjectHandler
index|[]
name|data
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
name|data
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|setObject
argument_list|(
name|data
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setStreamableReturn
parameter_list|(
name|CorbaStreamable
name|data
parameter_list|)
block|{
name|returnParam
operator|=
name|data
expr_stmt|;
block|}
specifier|public
name|void
name|setStreamableReturnValue
parameter_list|(
name|CorbaObjectHandler
name|data
parameter_list|)
block|{
comment|// TODO: Handle case of the return parameter has not yet been initialized.
name|returnParam
operator|.
name|setObject
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStreamableException
parameter_list|(
name|CorbaStreamable
name|ex
parameter_list|)
block|{
name|except
operator|=
name|ex
expr_stmt|;
block|}
specifier|public
name|void
name|setStreamableExceptionValue
parameter_list|(
name|CorbaObjectHandler
name|exData
parameter_list|)
block|{
comment|// TODO: Handle case of the return parameter has not yet been initialized.
name|except
operator|.
name|setObject
argument_list|(
name|exData
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CorbaStreamable
name|createStreamableObject
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|,
name|QName
name|elName
parameter_list|)
block|{
return|return
operator|new
name|CorbaStreamableImpl
argument_list|(
name|obj
argument_list|,
name|elName
argument_list|)
return|;
block|}
specifier|public
name|CorbaTypeMap
name|getCorbaTypeMap
parameter_list|()
block|{
return|return
name|corbaTypeMap
return|;
block|}
specifier|public
name|void
name|setCorbaTypeMap
parameter_list|(
name|CorbaTypeMap
name|typeMap
parameter_list|)
block|{
name|corbaTypeMap
operator|=
name|typeMap
expr_stmt|;
block|}
block|}
end_class

end_unit

