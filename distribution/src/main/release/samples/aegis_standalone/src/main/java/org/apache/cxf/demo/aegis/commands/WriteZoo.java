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
name|demo
operator|.
name|aegis
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLOutputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|AegisContext
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
name|AegisWriter
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
name|demo
operator|.
name|aegis
operator|.
name|types
operator|.
name|Animal
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
name|demo
operator|.
name|aegis
operator|.
name|types
operator|.
name|Zoo
import|;
end_import

begin_import
import|import
name|javanet
operator|.
name|staxutils
operator|.
name|IndentingXMLStreamWriter
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WriteZoo
block|{
specifier|private
name|XMLOutputFactory
name|outputFactory
decl_stmt|;
specifier|private
name|String
name|outputPathname
decl_stmt|;
specifier|private
name|WriteZoo
parameter_list|()
block|{
name|outputFactory
operator|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|go
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisContext
name|context
decl_stmt|;
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setWriteXsiTypes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|rootClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|Zoo
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|setRootClasses
argument_list|(
name|rootClasses
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|AegisWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|context
operator|.
name|createXMLStreamWriter
argument_list|()
decl_stmt|;
name|FileOutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outputPathname
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|outputFactory
operator|.
name|createXMLStreamWriter
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|IndentingXMLStreamWriter
name|indentWriter
init|=
operator|new
name|IndentingXMLStreamWriter
argument_list|(
name|xmlWriter
argument_list|)
decl_stmt|;
name|Zoo
name|zoo
init|=
name|populateZoo
argument_list|()
decl_stmt|;
name|AegisType
name|aegisType
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|zoo
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|zoo
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:aegis:demo"
argument_list|,
literal|"zoo"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|indentWriter
argument_list|,
name|aegisType
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Zoo
name|populateZoo
parameter_list|()
block|{
name|Zoo
name|zoo
init|=
operator|new
name|Zoo
argument_list|()
decl_stmt|;
name|zoo
operator|.
name|setFounder
argument_list|(
literal|"Noah"
argument_list|)
expr_stmt|;
name|zoo
operator|.
name|setName
argument_list|(
literal|"The Original Zoo"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Animal
argument_list|>
name|animals
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Animal
name|a
init|=
operator|new
name|Animal
argument_list|()
decl_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"lion"
argument_list|)
expr_stmt|;
name|animals
operator|.
name|put
argument_list|(
literal|"lion"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|Animal
argument_list|()
expr_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"tiger"
argument_list|)
expr_stmt|;
name|animals
operator|.
name|put
argument_list|(
literal|"tiger"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|Animal
argument_list|()
expr_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"bear"
argument_list|)
expr_stmt|;
name|animals
operator|.
name|put
argument_list|(
literal|"bear"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|zoo
operator|.
name|setAnimals
argument_list|(
name|animals
argument_list|)
expr_stmt|;
return|return
name|zoo
return|;
block|}
comment|/**      * @param args      * @throws Exception      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|WriteZoo
name|wz
init|=
operator|new
name|WriteZoo
argument_list|()
decl_stmt|;
name|wz
operator|.
name|outputPathname
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
name|wz
operator|.
name|go
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

