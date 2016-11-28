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
name|tools
operator|.
name|corba
operator|.
name|common
operator|.
name|idltypes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|logging
operator|.
name|Level
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

begin_class
specifier|public
specifier|final
class|class
name|IdlParam
extends|extends
name|IdlDefnImplBase
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
name|IdlParam
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|IdlType
name|type
decl_stmt|;
specifier|private
name|String
name|mode
decl_stmt|;
specifier|private
name|IdlParam
parameter_list|(
name|IdlOperation
name|parent
parameter_list|,
name|String
name|name
parameter_list|,
name|IdlType
name|typeType
parameter_list|,
name|String
name|modeValue
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|typeType
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|modeValue
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlParam
name|create
parameter_list|(
name|IdlOperation
name|parent
parameter_list|,
name|String
name|name
parameter_list|,
name|IdlType
name|type
parameter_list|,
name|String
name|mode
parameter_list|)
block|{
name|name
operator|=
name|CorbaUtils
operator|.
name|mangleName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
operator|new
name|IdlParam
argument_list|(
name|parent
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|mode
argument_list|)
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|isEmptyDef
argument_list|()
condition|)
block|{
name|IdlScopedName
name|sn
init|=
name|definedIn
argument_list|()
operator|.
name|scopeName
argument_list|()
decl_stmt|;
name|pw
operator|.
name|print
argument_list|(
name|indent
argument_list|()
operator|+
name|mode
operator|+
literal|" "
operator|+
name|type
operator|.
name|fullName
argument_list|(
name|sn
argument_list|)
operator|+
literal|" "
operator|+
name|localName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Ignoring Param "
operator|+
name|localName
argument_list|()
operator|+
literal|" with Empty Type"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isEmptyDef
parameter_list|()
block|{
return|return
name|type
operator|.
name|isEmptyDef
argument_list|()
return|;
block|}
specifier|public
name|IdlScopeBase
name|getCircularScope
parameter_list|(
name|IdlScopeBase
name|startScope
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|doneDefn
parameter_list|)
block|{
return|return
name|type
operator|.
name|getCircularScope
argument_list|(
name|startScope
argument_list|,
name|doneDefn
argument_list|)
return|;
block|}
block|}
end_class

end_unit

